// Common JavaScript functions

// Check authentication on page load
document.addEventListener("DOMContentLoaded", function () {
  checkAuth();
  updateUserInfo();
});

function checkAuth() {
  const currentUser = localStorage.getItem("currentUser");

  if (!currentUser && !window.location.pathname.includes("index.html")) {
    window.location.href = "index.html";
    return;
  }

  if (currentUser && window.location.pathname.includes("index.html")) {
    window.location.href = "dashboard.html";
    return;
  }
}

function updateUserInfo() {
  const currentUser = localStorage.getItem("currentUser");

  if (currentUser) {
    const user = JSON.parse(currentUser);
    const userFullNameElement = document.getElementById("userFullName");

    if (userFullNameElement) {
      userFullNameElement.textContent = user.fullName || user.username;
    }
  }
}

// Utility function to format currency
function formatCurrency(amount) {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    minimumFractionDigits: 2,
  }).format(amount);
}

// Utility function to format numbers
function formatNumber(number) {
  return new Intl.NumberFormat("en-IN").format(number);
}

// Utility function to format date
function formatDate(date) {
  return new Date(date).toLocaleDateString("en-IN", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}

// Utility function to format datetime
function formatDateTime(datetime) {
  return new Date(datetime).toLocaleString("en-IN", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

// Show loading spinner
function showLoading(elementId, show = true) {
  const element = document.getElementById(elementId);

  if (show) {
    element.innerHTML =
      '<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Loading...</div>';
  }
}

// Show success message
function showSuccess(message, duration = 3000) {
  showToast(message, "success", duration);
}

// Show error message
function showError(message, duration = 5000) {
  showToast(message, "danger", duration);
}

// Show warning message
function showWarning(message, duration = 4000) {
  showToast(message, "warning", duration);
}

// Show info message
function showInfo(message, duration = 3000) {
  showToast(message, "info", duration);
}

// Generic toast function
function showToast(message, type = "info", duration = 3000) {
  // Create toast container if it doesn't exist
  let toastContainer = document.getElementById("toastContainer");
  if (!toastContainer) {
    toastContainer = document.createElement("div");
    toastContainer.id = "toastContainer";
    toastContainer.className = "toast-container position-fixed top-0 end-0 p-3";
    toastContainer.style.zIndex = "9999";
    document.body.appendChild(toastContainer);
  }

  const toastId = "toast-" + Date.now();
  const toastHtml = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type} border-0" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;

  toastContainer.insertAdjacentHTML("beforeend", toastHtml);

  const toastElement = document.getElementById(toastId);
  const toast = new bootstrap.Toast(toastElement, {
    delay: duration,
  });

  toast.show();

  // Remove toast element after it's hidden
  toastElement.addEventListener("hidden.bs.toast", function () {
    toastElement.remove();
  });
}

// Confirm dialog
function confirmAction(message, callback) {
  if (confirm(message)) {
    callback();
  }
}

// API request helper
async function apiRequest(url, options = {}) {
  try {
    const response = await fetch(url, {
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      ...options,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("API request failed:", error);
    throw error;
  }
}

// Get current user
function getCurrentUser() {
  const currentUser = localStorage.getItem("currentUser");
  return currentUser ? JSON.parse(currentUser) : null;
}

// Check if user has permission
function hasPermission(permission) {
  const user = getCurrentUser();

  if (!user) return false;

  // Admin has all permissions
  if (user.role === "admin") return true;

  // Define role-based permissions
  const permissions = {
    manager: ["read", "create", "update", "reports"],
    cashier: ["read", "sales"],
  };

  return permissions[user.role]?.includes(permission) || false;
}

// Debounce function for search
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

// Export functions for use in other scripts
window.RetailIMS = {
  formatCurrency,
  formatNumber,
  formatDate,
  formatDateTime,
  showLoading,
  showSuccess,
  showError,
  showWarning,
  showInfo,
  confirmAction,
  apiRequest,
  getCurrentUser,
  hasPermission,
  debounce,
};
