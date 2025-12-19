const prevBtns = document.querySelectorAll(".btn-prev");
const nextBtns = document.querySelectorAll(".btn-next");
const clearBtns = document.querySelectorAll(".btn-clear");
const submitBtns = document.querySelectorAll(".btn-submit");
const progress = document.getElementById("progress");
const formSteps = document.querySelectorAll(".form-box");
const progressSteps = document.querySelectorAll(".progress-step");

let formStepsNum = 0;


nextBtns.forEach((btn) => {
  btn.addEventListener("click", () => {
    formStepsNum++;
    updateFormSteps();
    updateProgressbar();
  });
});

 
prevBtns.forEach((btn) => {
  btn.addEventListener("click", () => {
    formStepsNum--;
    updateFormSteps();
    updateProgressbar();
  });
});

clearBtns.forEach((btn) => {
  btn.addEventListener("click", () => {
    clearCurrentSection();
  });
});

// const BASE_URL = "https://localhost:8081/api/register";
const BASE_URL = "http://localhost:8081";

submitBtns.forEach((btn) => {
  btn.addEventListener("click", async (event) => {
      event.preventDefault();
      showLoader(); // Show loader
      try {
          const requiredCheckboxes = document.querySelectorAll("input[type='checkbox'][required]");
          const allChecked = Array.from(requiredCheckboxes).every((checkbox) => checkbox.checked);

          if (!allChecked) {
              showPrompt("Please check all required checkboxes!", "error");
              hideLoader(); // Hide loader
              return;
          }

          const formData = gatherFormData();
          console.log(formData);
          const response = await fetch(`${BASE_URL}/api/register`, {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(formData),
          });

          const result = await response.json();

          if (result.status === "OK") {
              showPrompt(result.message, "success");
              setTimeout(() => {
                  window.location.href = "index.html"; // Redirect after a short delay
              }, 500);
          } else {
              showPrompt(result.message, "error");
          }
      } catch (error) {
          console.log(error);
          showPrompt("Failed to connect to the server. Please try again later.", "error");
      } finally {
          hideLoader(); // Hide loader
      }
  });
});




  function gatherFormData() {
    try {
        const getValueOrNull = (selector) => {
            const element = document.querySelector(selector);
            return element && element.value.trim() ? element.value.trim() : null;
        };

        const getCheckedValueOrNull = (selector) => {
            const element = document.querySelector(selector);
            return element && element.checked ? element.id : null;
        };

        // Extract email
        const email = getValueOrNull("input[name='email']");

        // Determine type: Band or Individual
        const type = getCheckedValueOrNull("input[name='band-type']:checked") === "band" ? "Band" : "Individual";

        // Extract name based on type
        const name =
            type === "Band"
                ? getValueOrNull(".band-box input[placeholder='Band name*']")
                : getValueOrNull(".ind-box input[placeholder='Your name*']");

        // Extract leaderName for Band type or set null for Individual type
        const leaderName =
            type === "Band"
                ? getValueOrNull(".band-leader-box input[placeholder=\"Band Leader's name*\"]")
                : null;

        // Extract phoneNumber based on type
        const phoneNumber = type === "Band"
            ? getValueOrNull(".band-leader-box input[placeholder='Contact No.*']")
            : getValueOrNull(".ind-box input[placeholder='Contact No.*']");

        // Extract city based on type
        const city =
            type === "Band"
                ? getValueOrNull(".band-box input[placeholder='Which city is your band based at?*']")
                : getValueOrNull(".ind-box input[placeholder='City*']");

        // Extract details based on type
        const details =
            type === "Band"
                ? getValueOrNull(".band-box textarea")
                : getValueOrNull(".ind-box input[placeholder='Enter the instrument\\'s name here']");

        // Extract accommodation requirement
        const accommodationRequired =
            getCheckedValueOrNull("input[name='yes-type']:checked") === "yes" ? "Yes" : "No";

        // Extract Google Drive link
        const driveLink = getValueOrNull(".upload input[placeholder='Enter the link here']");

        // Construct and return the JSON object
        return {
            email,
            type,
            name,
            leaderName,
            phoneNumber,
            city,
            details,
            accommodationRequired,
            driveLink,
        };
    } catch (error) {
        console.error("Error generating JSON:", error);
        return null;
    }
}
  

function updateFormSteps() {
  // Toggle active form step
  formSteps.forEach((formStep) => {
    formStep.classList.remove("form-box-active");
  });

  formSteps[formStepsNum].classList.add("form-box-active");

  toggleSubmitButton();
}

function updateProgressbar() {
  progressSteps.forEach((progressStep, idx) => {
    if (idx < formStepsNum + 1) {
      progressStep.classList.add("progress-step-active");
    } else {
      progressStep.classList.remove("progress-step-active");
    }
  });

  const progressActive = document.querySelectorAll(".progress-step-active");
  progress.style.width =
    ((progressActive.length - 1) / (progressSteps.length - 1)) * 100 + "%";
}

function clearCurrentSection() {
  const currentSection = formSteps[formStepsNum];
  const inputs = currentSection.querySelectorAll(".input-field");
  const checkboxes = currentSection.querySelectorAll("input[type='checkbox']");

  inputs.forEach((input) => {
    input.value = "";
  });

  checkboxes.forEach((checkbox) => {
    checkbox.checked = false;
  });
}

function toggleSubmitButton() {
  const lastPage = formStepsNum === formSteps.length - 1;

  submitBtns.forEach((btn) => {
    btn.style.display = lastPage ? "inline-block" : "none";
  });

  clearBtns.forEach((btn) => {
    btn.style.justifyContent = lastPage ? "flex-start" : "flex-end";
  });
}


// Get radio buttons and the corresponding sections
const bandRadio = document.getElementById("band");
const individualRadio = document.getElementById("individual");
const bandBox = document.querySelector(".band-box");
const indBox = document.querySelector(".ind-box");

// Function to toggle visibility based on selection
function toggleSections() {
    if (bandRadio.checked) {
        bandBox.style.display = "block"; // Show band section
        indBox.style.display = "none";  // Hide individual section
    } else if (individualRadio.checked) {
        indBox.style.display = "block"; // Show individual section
        bandBox.style.display = "none";  // Hide band section
    }
}

// Event listeners for radio buttons
bandRadio.addEventListener("change", toggleSections);
individualRadio.addEventListener("change", toggleSections);

// Initial visibility setup
toggleSections(); // Call the function once to set the initial state

// popup
// document.addEventListener('DOMContentLoaded', () => {
//   const rulesLinks = document.querySelectorAll('.register-link a, .remember-forget a'); // Select both rules links
//   const popup = document.getElementById('rules-popup'); // Popup element
//   const closeBtn = document.querySelector('.close-btn'); // Close button in the popup

//   // Show the popup
//   rulesLinks.forEach(link => {
//     link.addEventListener('click', (event) => {
//       event.preventDefault(); // Prevent default anchor action
//       popup.style.display = 'flex'; // Show the popup
//     });
//   });

//   // Hide the popup on close button click
//   closeBtn.addEventListener('click', () => {
//     popup.style.display = 'none'; // Hide the popup
//   });

//   // Hide the popup when clicking outside the popup content
//   window.addEventListener('click', (event) => {
//     if (event.target === popup) {
//       popup.style.display = 'none'; // Hide the popup
//     }
//   });
// });



function showPrompt(message, type) {
    const promptContainer = document.createElement("div");
    promptContainer.className = `custom-prompt ${type}`;
    promptContainer.innerHTML = `
    <div class="prompt-icon"></div>
    <div class="prompt-message">${message}</div>
  `;
    document.body.appendChild(promptContainer);
  
    // Auto-remove the prompt after a short duration
    setTimeout(() => {
      promptContainer.classList.add("fade-out");
      promptContainer.addEventListener("animationend", () => {
        promptContainer.remove();
      });
    }, 3000);
  }

  function showLoader() {
    const loader = document.createElement("div");
    loader.id = "loader";
    loader.innerHTML = `
        <div class="loader-animation"></div>
        <p>Please Wait...</p>
    `;
    document.body.appendChild(loader);
}

function hideLoader() {
    const loader = document.getElementById("loader");
    if (loader) {
        loader.remove();
    }
}
