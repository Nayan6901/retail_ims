// Dashboard JavaScript

let salesChart;

document.addEventListener("DOMContentLoaded", function () {
  loadDashboardData();
  initializeCharts();
});

async function loadDashboardData() {
  try {
    // Load dashboard statistics
    await loadDashboardStats();

    // Load low stock products
    await loadLowStockProducts();

    // Load top selling products
    await loadTopProducts();

    // Load weekly sales data for chart
    await loadWeeklySalesData();
  } catch (error) {
    console.error("Error loading dashboard data:", error);
    showError("Failed to load dashboard data");
  }
}

async function loadDashboardStats() {
  try {
    showLoading("totalProducts");

    const stats = await apiRequest("/api/dashboard/stats");

    // Update stat cards
    document.getElementById("totalProducts").textContent = formatNumber(
      stats.totalProducts || 0
    );
    document.getElementById("lowStockCount").textContent = formatNumber(
      stats.lowStockCount || 0
    );
    document.getElementById("todaySales").textContent = formatNumber(
      stats.todaySales || 0
    );
    document.getElementById("monthSales").textContent = formatNumber(
      stats.monthSales || 0
    );
  } catch (error) {
    console.error("Error loading dashboard stats:", error);
    // Set default values
    document.getElementById("totalProducts").textContent = "0";
    document.getElementById("lowStockCount").textContent = "0";
    document.getElementById("todaySales").textContent = "0";
    document.getElementById("monthSales").textContent = "0";
  }
}

async function loadLowStockProducts() {
  const tableBody = document.getElementById("lowStockTable");

  try {
    showLoading("lowStockTable");

    // Fetch low stock products from API
    const lowStockProducts = await apiRequest("/api/products/low-stock");

    tableBody.innerHTML = "";

    if (lowStockProducts.length === 0) {
      tableBody.innerHTML =
        '<tr><td colspan="4" class="text-center text-muted">No low stock items</td></tr>';
      return;
    }

    lowStockProducts.forEach((product) => {
      const stockLevel = product.currentStock;
      const minLevel = product.minStockLevel;

      // Determine badge color based on stock level
      let badgeClass = "bg-warning";
      if (stockLevel === 0) {
        badgeClass = "bg-danger";
      } else if (stockLevel <= minLevel / 2) {
        badgeClass = "bg-danger";
      }

      const row = `
                <tr>
                    <td>
                        <div class="fw-bold">${product.productName}</div>
                        <small class="text-muted">${product.productCode}</small>
                    </td>
                    <td><span class="badge ${badgeClass}">${stockLevel}</span></td>
                    <td>${minLevel}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="reorderProduct(${product.productId})" title="Reorder">
                            <i class="fas fa-shopping-cart"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-info ms-1" onclick="viewProduct(${product.productId})" title="View Details">
                            <i class="fas fa-eye"></i>
                        </button>
                    </td>
                </tr>
            `;
      tableBody.insertAdjacentHTML("beforeend", row);
    });
  } catch (error) {
    console.error("Error loading low stock products:", error);
    tableBody.innerHTML =
      '<tr><td colspan="4" class="text-center text-danger">Error loading low stock data</td></tr>';
  }
}

async function loadTopProducts() {
  const tableBody = document.getElementById("topProductsTable");

  try {
    showLoading("topProductsTable");

    const topProducts = await apiRequest("/api/dashboard/top-products");

    tableBody.innerHTML = "";

    if (Object.keys(topProducts).length === 0) {
      tableBody.innerHTML =
        '<tr><td colspan="3" class="text-center text-muted">No sales data available</td></tr>';
      return;
    }

    Object.entries(topProducts).forEach(([productName, quantity], index) => {
      const trendIcon =
        index < 2 ? "fa-arrow-up text-success" : "fa-arrow-right text-warning";

      const row = `
                <tr>
                    <td>${productName}</td>
                    <td><span class="badge bg-success">${quantity}</span></td>
                    <td><i class="fas ${trendIcon}"></i></td>
                </tr>
            `;
      tableBody.insertAdjacentHTML("beforeend", row);
    });
  } catch (error) {
    console.error("Error loading top products:", error);
    tableBody.innerHTML =
      '<tr><td colspan="3" class="text-center text-danger">Error loading data</td></tr>';
  }
}

async function loadWeeklySalesData() {
  try {
    const salesData = await apiRequest("/api/dashboard/weekly-sales");

    // Prepare data for chart
    const labels = [];
    const data = [];

    // Get last 7 days
    for (let i = 6; i >= 0; i--) {
      const date = new Date();
      date.setDate(date.getDate() - i);
      const dateStr = date.toISOString().split("T")[0];
      const label = date.toLocaleDateString("en-IN", {
        month: "short",
        day: "numeric",
      });

      labels.push(label);
      data.push(salesData[dateStr] || 0);
    }

    updateSalesChart(labels, data);
  } catch (error) {
    console.error("Error loading weekly sales data:", error);
    // Use dummy data
    const labels = [
      "Oct 1",
      "Oct 2",
      "Oct 3",
      "Oct 4",
      "Oct 5",
      "Oct 6",
      "Oct 7",
    ];
    const data = [12000, 15000, 18000, 14000, 22000, 19000, 25000];
    updateSalesChart(labels, data);
  }
}

function initializeCharts() {
  const ctx = document.getElementById("salesChart").getContext("2d");

  salesChart = new Chart(ctx, {
    type: "line",
    data: {
      labels: [],
      datasets: [
        {
          label: "Sales (₹)",
          data: [],
          borderColor: "rgb(13, 110, 253)",
          backgroundColor: "rgba(13, 110, 253, 0.1)",
          borderWidth: 3,
          fill: true,
          tension: 0.4,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false,
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function (value) {
              return "₹" + formatNumber(value);
            },
          },
        },
      },
      elements: {
        point: {
          radius: 6,
          hoverRadius: 8,
        },
      },
    },
  });
}

function updateSalesChart(labels, data) {
  if (salesChart) {
    salesChart.data.labels = labels;
    salesChart.data.datasets[0].data = data;
    salesChart.update();
  }
}

function reorderProduct(productId) {
  showInfo(
    `Reorder functionality for product ${productId} would be implemented here`
  );
  // Implement reorder functionality
}

function viewProduct(productId) {
  // Redirect to products page with specific product highlighted
  window.location.href = `products.html?highlight=${productId}`;
}

// Refresh dashboard data
function refreshDashboard() {
  showInfo("Refreshing dashboard...");
  loadDashboardData();
}
