// Authentication JavaScript

document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("loginForm");
  const alertContainer = document.getElementById("alertContainer");

  // Check if user is already logged in
  checkLoginStatus();

  if (loginForm) {
    loginForm.addEventListener("submit", handleLogin);
  }

  async function handleLogin(e) {
    e.preventDefault();

    const formData = new FormData(loginForm);
    const username = formData.get("username");
    const password = formData.get("password");

    if (!username || !password) {
      showAlert("Please enter both username and password", "warning");
      return;
    }

    try {
      showLoading(true);

      const response = await fetch("/auth/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `username=${encodeURIComponent(
          username
        )}&password=${encodeURIComponent(password)}`,
      });

      const result = await response.json();

      if (result.success) {
        // Store user data in localStorage
        localStorage.setItem("currentUser", JSON.stringify(result.user));

        showAlert("Login successful! Redirecting...", "success");

        // Redirect to dashboard
        setTimeout(() => {
          window.location.href = "dashboard.html";
        }, 1500);
      } else {
        showAlert(result.message || "Login failed", "danger");
      }
    } catch (error) {
      console.error("Login error:", error);
      showAlert("Connection error. Please try again.", "danger");
    } finally {
      showLoading(false);
    }
  }

  function checkLoginStatus() {
    const currentUser = localStorage.getItem("currentUser");

    if (currentUser && window.location.pathname.includes("index.html")) {
      // User is logged in and on login page, redirect to dashboard
      window.location.href = "dashboard.html";
    } else if (
      !currentUser &&
      !window.location.pathname.includes("index.html")
    ) {
      // User is not logged in and not on login page, redirect to login
      window.location.href = "index.html";
    }
  }

  function showAlert(message, type = "info") {
    const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
    alertContainer.innerHTML = alertHtml;
  }

  function showLoading(show) {
    const submitBtn = document.querySelector(
      '#loginForm button[type="submit"]'
    );
    if (show) {
      submitBtn.disabled = true;
      submitBtn.innerHTML =
        '<i class="fas fa-spinner fa-spin me-2"></i>Signing in...';
    } else {
      submitBtn.disabled = false;
      submitBtn.innerHTML = '<i class="fas fa-sign-in-alt me-2"></i>Sign In';
    }
  }
});

// Logout function
async function logout() {
  try {
    await fetch("/auth/api/logout", {
      method: "POST",
    });
  } catch (error) {
    console.error("Logout error:", error);
  } finally {
    // Clear user data
    localStorage.removeItem("currentUser");

    // Redirect to login
    window.location.href = "index.html";
  }
}
