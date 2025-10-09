// Reports JavaScript

let reportChart = null;
let currentReportData = [];

document.addEventListener("DOMContentLoaded", function () {
  setDefaultDates();
  loadQuickStats();
});

function setDefaultDates() {
  const today = new Date();
  const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

  document.getElementById("startDate").value = firstDayOfMonth
    .toISOString()
    .split("T")[0];
  document.getElementById("endDate").value = today.toISOString().split("T")[0];
}

async function loadQuickStats() {
  try {
    const stats = await fetch("/api/reports/quick-stats");
    if (stats.ok) {
      const data = await stats.json();
      document.getElementById("todaySalesReport").textContent = formatNumber(
        data.todaySales || 0
      );
      document.getElementById("monthSalesReport").textContent = formatNumber(
        data.monthSales || 0
      );
      document.getElementById("lowStockCountReport").textContent =
        data.lowStockCount || 0;
      document.getElementById("totalProductsReport").textContent =
        data.totalProducts || 0;
    }
  } catch (error) {
    console.error("Error loading quick stats:", error);
  }
}

function onReportTypeChange() {
  const reportType = document.getElementById("reportType").value;
  const reportCard = document.getElementById("reportCard");

  if (!reportType) {
    reportCard.style.display = "none";
    return;
  }

  // Show relevant date filters based on report type
  const startDate = document.getElementById("startDate");
  const endDate = document.getElementById("endDate");

  if (reportType === "inventory" || reportType === "low-stock") {
    startDate.disabled = true;
    endDate.disabled = true;
  } else {
    startDate.disabled = false;
    endDate.disabled = false;
  }
}

async function generateReport() {
  const reportType = document.getElementById("reportType").value;
  const startDate = document.getElementById("startDate").value;
  const endDate = document.getElementById("endDate").value;

  if (!reportType) {
    showError("Please select a report type");
    return;
  }

  showLoading();

  try {
    let endpoint = `/api/reports/${reportType}`;
    const params = new URLSearchParams();

    if (startDate) params.append("startDate", startDate);
    if (endDate) params.append("endDate", endDate);

    if (params.toString()) {
      endpoint += "?" + params.toString();
    }

    const response = await fetch(endpoint);
    if (!response.ok) {
      throw new Error("Failed to generate report");
    }

    const data = await response.json();
    displayReport(reportType, data);
  } catch (error) {
    console.error("Error generating report:", error);
    showError("Failed to generate report: " + error.message);
    showEmptyState();
  }
}

function showLoading() {
  const reportCard = document.getElementById("reportCard");
  const loadingState = document.getElementById("loadingState");
  const chartContainer = document.getElementById("chartContainer");
  const tableContainer = document.getElementById("tableContainer");
  const emptyState = document.getElementById("emptyState");

  reportCard.style.display = "block";
  loadingState.style.display = "block";
  chartContainer.style.display = "none";
  tableContainer.style.display = "none";
  emptyState.style.display = "none";
}

function showEmptyState() {
  const loadingState = document.getElementById("loadingState");
  const chartContainer = document.getElementById("chartContainer");
  const tableContainer = document.getElementById("tableContainer");
  const emptyState = document.getElementById("emptyState");

  loadingState.style.display = "none";
  chartContainer.style.display = "none";
  tableContainer.style.display = "none";
  emptyState.style.display = "block";
}

function displayReport(reportType, data) {
  const loadingState = document.getElementById("loadingState");
  const chartContainer = document.getElementById("chartContainer");
  const tableContainer = document.getElementById("tableContainer");
  const emptyState = document.getElementById("emptyState");
  const reportTitle = document.getElementById("reportTitle");

  loadingState.style.display = "none";

  if (!data || (Array.isArray(data) && data.length === 0)) {
    showEmptyState();
    return;
  }

  currentReportData = data;

  // Update report title
  const titles = {
    sales: "Sales Report",
    inventory: "Inventory Report",
    "low-stock": "Low Stock Report",
    products: "Product Performance Report",
    categories: "Category Performance Report",
    suppliers: "Supplier Report",
    profit: "Profit Analysis Report",
  };
  reportTitle.innerHTML = `<i class="fas fa-chart-bar me-2"></i>${
    titles[reportType] || "Report Results"
  }`;

  switch (reportType) {
    case "sales":
      displaySalesReport(data);
      break;
    case "inventory":
      displayInventoryReport(data);
      break;
    case "low-stock":
      displayLowStockReport(data);
      break;
    case "products":
      displayProductsReport(data);
      break;
    case "categories":
      displayCategoriesReport(data);
      break;
    case "suppliers":
      displaySuppliersReport(data);
      break;
    case "profit":
      displayProfitReport(data);
      break;
    default:
      showEmptyState();
  }
}

function displaySalesReport(data) {
  showTable();
  createTable(
    ["Date", "Sale ID", "Customer", "Items", "Total Amount", "Payment Method"],
    data.map((sale) => [
      new Date(sale.saleDate).toLocaleDateString(),
      "#" + sale.saleNumber,
      sale.customerName || "Walk-in Customer",
      sale.itemCount || 0,
      "₹" + sale.totalAmount.toFixed(2),
      sale.paymentMethod,
    ])
  );

  // Create chart if data has daily summaries
  if (data.length > 0) {
    createSalesChart(data);
  }
}

function displayInventoryReport(data) {
  showTable();
  createTable(
    [
      "Product Code",
      "Product Name",
      "Category",
      "Current Stock",
      "Min Level",
      "Status",
    ],
    data.map((product) => [
      product.productCode,
      product.productName,
      product.categoryName || "N/A",
      product.currentStock,
      product.minStockLevel,
      product.currentStock <= product.minStockLevel
        ? '<span class="badge bg-danger">Low Stock</span>'
        : '<span class="badge bg-success">In Stock</span>',
    ])
  );
}

function displayLowStockReport(data) {
  showTable();
  createTable(
    [
      "Product Code",
      "Product Name",
      "Current Stock",
      "Min Level",
      "Shortage",
      "Action Required",
    ],
    data.map((product) => [
      product.productCode,
      product.productName,
      product.currentStock,
      product.minStockLevel,
      product.minStockLevel - product.currentStock,
      '<button class="btn btn-sm btn-warning">Reorder</button>',
    ])
  );
}

function displayProductsReport(data) {
  showTable();
  createTable(
    ["Product Name", "Total Sold", "Revenue", "Profit", "Avg. Price"],
    data.map((product) => [
      product.productName,
      product.totalSold || 0,
      "₹" + (product.revenue || 0).toFixed(2),
      "₹" + (product.profit || 0).toFixed(2),
      "₹" + (product.avgPrice || 0).toFixed(2),
    ])
  );

  // Create product performance chart
  if (data.length > 0) {
    createProductChart(data);
  }
}

function displayCategoriesReport(data) {
  showTable();
  createTable(
    ["Category", "Products", "Total Sales", "Revenue", "Market Share"],
    data.map((category) => [
      category.categoryName,
      category.productCount || 0,
      category.totalSales || 0,
      "₹" + (category.revenue || 0).toFixed(2),
      (category.marketShare || 0).toFixed(1) + "%",
    ])
  );

  // Create category chart
  if (data.length > 0) {
    createCategoryChart(data);
  }
}

function displaySuppliersReport(data) {
  showTable();
  createTable(
    [
      "Supplier",
      "Products Supplied",
      "Total Purchases",
      "Last Purchase",
      "Status",
    ],
    data.map((supplier) => [
      supplier.supplierName,
      supplier.productCount || 0,
      "₹" + (supplier.totalPurchases || 0).toFixed(2),
      supplier.lastPurchase
        ? new Date(supplier.lastPurchase).toLocaleDateString()
        : "N/A",
      supplier.isActive
        ? '<span class="badge bg-success">Active</span>'
        : '<span class="badge bg-secondary">Inactive</span>',
    ])
  );
}

function displayProfitReport(data) {
  showTable();
  createTable(
    [
      "Product",
      "Cost Price",
      "Selling Price",
      "Profit Margin",
      "Units Sold",
      "Total Profit",
    ],
    data.map((item) => [
      item.productName,
      "₹" + item.costPrice.toFixed(2),
      "₹" + item.sellingPrice.toFixed(2),
      item.profitMargin.toFixed(1) + "%",
      item.unitsSold || 0,
      "₹" + (item.totalProfit || 0).toFixed(2),
    ])
  );

  // Create profit chart
  if (data.length > 0) {
    createProfitChart(data);
  }
}

function showTable() {
  const chartContainer = document.getElementById("chartContainer");
  const tableContainer = document.getElementById("tableContainer");

  chartContainer.style.display = "block";
  tableContainer.style.display = "block";
}

function createTable(headers, rows) {
  const thead = document.getElementById("reportTableHead");
  const tbody = document.getElementById("reportTableBody");

  // Create headers
  thead.innerHTML =
    "<tr>" + headers.map((h) => `<th>${h}</th>`).join("") + "</tr>";

  // Create rows
  tbody.innerHTML = rows
    .map(
      (row) => "<tr>" + row.map((cell) => `<td>${cell}</td>`).join("") + "</tr>"
    )
    .join("");
}

function createSalesChart(data) {
  const ctx = document.getElementById("reportChart").getContext("2d");

  if (reportChart) {
    reportChart.destroy();
  }

  // Group sales by date
  const salesByDate = {};
  data.forEach((sale) => {
    const date = new Date(sale.saleDate).toDateString();
    salesByDate[date] = (salesByDate[date] || 0) + sale.totalAmount;
  });

  reportChart = new Chart(ctx, {
    type: "line",
    data: {
      labels: Object.keys(salesByDate),
      datasets: [
        {
          label: "Daily Sales",
          data: Object.values(salesByDate),
          borderColor: "rgb(75, 192, 192)",
          backgroundColor: "rgba(75, 192, 192, 0.2)",
          tension: 0.1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Sales Trend",
        },
      },
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });
}

function createProductChart(data) {
  const ctx = document.getElementById("reportChart").getContext("2d");

  if (reportChart) {
    reportChart.destroy();
  }

  reportChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: data.slice(0, 10).map((p) => p.productName),
      datasets: [
        {
          label: "Revenue",
          data: data.slice(0, 10).map((p) => p.revenue || 0),
          backgroundColor: "rgba(54, 162, 235, 0.8)",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Top 10 Products by Revenue",
        },
      },
    },
  });
}

function createCategoryChart(data) {
  const ctx = document.getElementById("reportChart").getContext("2d");

  if (reportChart) {
    reportChart.destroy();
  }

  reportChart = new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: data.map((c) => c.categoryName),
      datasets: [
        {
          data: data.map((c) => c.revenue || 0),
          backgroundColor: [
            "#FF6384",
            "#36A2EB",
            "#FFCE56",
            "#4BC0C0",
            "#9966FF",
            "#FF9F40",
            "#FF6384",
            "#C9CBCF",
          ],
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Revenue by Category",
        },
        legend: {
          position: "bottom",
        },
      },
    },
  });
}

function createProfitChart(data) {
  const ctx = document.getElementById("reportChart").getContext("2d");

  if (reportChart) {
    reportChart.destroy();
  }

  reportChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: data.slice(0, 10).map((p) => p.productName),
      datasets: [
        {
          label: "Profit Margin %",
          data: data.slice(0, 10).map((p) => p.profitMargin || 0),
          backgroundColor: "rgba(75, 192, 192, 0.8)",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Top 10 Products by Profit Margin",
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function (value) {
              return value + "%";
            },
          },
        },
      },
    },
  });
}

function printReport() {
  window.print();
}

function exportReport() {
  if (!currentReportData || currentReportData.length === 0) {
    showError("No data to export");
    return;
  }

  const reportType = document.getElementById("reportType").value;
  const startDate = document.getElementById("startDate").value;
  const endDate = document.getElementById("endDate").value;

  // Create CSV content
  let csv = "";
  const table = document.getElementById("reportTable");
  const rows = table.querySelectorAll("tr");

  rows.forEach((row) => {
    const cells = row.querySelectorAll("th, td");
    const rowData = Array.from(cells).map((cell) => {
      // Clean HTML tags and escape quotes
      const text = cell.textContent.trim();
      return '"' + text.replace(/"/g, '""') + '"';
    });
    csv += rowData.join(",") + "\n";
  });

  // Download CSV file
  const blob = new Blob([csv], { type: "text/csv" });
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `${reportType}_report_${startDate}_to_${endDate}.csv`;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(url);

  showSuccess("Report exported successfully");
}

// Utility functions
function formatNumber(num) {
  return new Intl.NumberFormat("en-IN").format(num);
}

function showError(message) {
  // Reuse the common.js showAlert function if available
  if (typeof showAlert === "function") {
    showAlert(message, "danger");
  } else {
    alert(message);
  }
}

function showSuccess(message) {
  // Reuse the common.js showAlert function if available
  if (typeof showAlert === "function") {
    showAlert(message, "success");
  } else {
    alert(message);
  }
}
