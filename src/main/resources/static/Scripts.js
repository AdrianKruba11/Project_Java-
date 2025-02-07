document.addEventListener("DOMContentLoaded", () => {
    const slides = document.querySelector(".slides");
    const slideImages = document.querySelectorAll(".thumb");
    const prevButton = document.querySelector(".prev");
    const nextButton = document.querySelector(".next");

    let currentIndex = 1; // Start at 1 to account for cloned slides
    const slideWidth = slideImages[0].clientWidth;
    let isTransitioning = false; // Flag to prevent spamming
    const autoScrollInterval = 5000;
    let autoScroll;

    // Clone the first and last slides
    const firstClone = slideImages[0].cloneNode(true);
    const lastClone = slideImages[slideImages.length - 1].cloneNode(true);
    slides.appendChild(firstClone);
    slides.insertBefore(lastClone, slideImages[0]);

    // Adjust slide position and count
    const allSlides = document.querySelectorAll(".thumb"); // Includes clones
    const totalSlides = allSlides.length;

    // Set initial position
    slides.style.transform = `translateX(-${currentIndex * slideWidth}px)`;

    const updateSlidePosition = () => {
        isTransitioning = true; // Block interactions
        slides.style.transition = "transform 0.5s ease-in-out";
        slides.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
    };

    const resetSlidePosition = () => {
        slides.style.transition = "none"; // Remove transition temporarily
        if (currentIndex === 0) {
            currentIndex = allSlides.length - 2; // Move to the last original slide
        } else if (currentIndex === allSlides.length - 1) {
            currentIndex = 1; // Move to the first original slide
        }
        slides.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
        isTransitioning = false; // Re-enable interactions
    };

    // Prevent rapid clicks
    const safeSlide = (direction) => {
        if (isTransitioning) return;
        currentIndex += direction;
        updateSlidePosition();
        resetAutoScroll();
    };

    // Event listeners for navigation buttons
    prevButton.addEventListener("click", () => safeSlide(-1));
    nextButton.addEventListener("click", () => safeSlide(1));

    // Auto scroll function
    const startAutoScroll = () => {
        autoScroll = setInterval(() => {
            safeSlide(1);
        }, autoScrollInterval);
    };

    const resetAutoScroll = () => {
        clearInterval(autoScroll);
        startAutoScroll();
    };

    // Transition end event for seamless loop
    slides.addEventListener("transitionend", resetSlidePosition);

    // Handle window resize
    window.addEventListener("resize", () => {
        slides.style.transition = "none"; // Disable transition during resize
        slides.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
    });

    // Handle visibility change
    document.addEventListener("visibilitychange", () => {
        if (document.hidden) {
            clearInterval(autoScroll);
        } else {
            startAutoScroll();
        }
    });

    // Initialize auto scroll on page load
    startAutoScroll();
});



//////////////////////////////////////////
/////////////////////////////////////////

// Check if the user is logged in when the page loads
window.onload = function() {
    const usernameDisplay = document.getElementById('usernameDisplay');
    const username = usernameDisplay.querySelector('strong').innerText.trim();

    console.log("Username found: " + username); // Debugging log to check username

    if (username && username !== "Brak użytkownika") {
        // User is logged in, show the logout link and hide the login and register links
        console.log("User is logged in. Showing logout link.");
        document.getElementById('authLink1').style.display = 'none';  // Hide Login link
        document.getElementById('authLink2').style.display = 'none';  // Hide Register link
        document.getElementById('logoutLink').style.display = 'inline';  // Show Logout link
    } else {
        // User is not logged in, show the login and register links and hide the logout link
        console.log("User is not logged in. Showing login and register links.");
        document.getElementById('authLink1').style.display = 'inline';  // Show Login link
        document.getElementById('authLink2').style.display = 'inline';  // Show Register link
        document.getElementById('logoutLink').style.display = 'none';   // Hide Logout link
    }
};


// Login function
async function submitLogin() {
    const login = document.getElementById('login').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({ login, password })
        });

        const message = await response.text();
        document.getElementById('responseMessage').innerText = response.ok
            ? `Success: ${message}`
            : `Error: ${message}`;

        if (response.ok) {
            // Reload the page to update the session status
            window.location.reload();
        }
    } catch (error) {
        document.getElementById('responseMessage').innerText = `Error: ${error.message}`;
    }
}

// Updated Logout function
async function submitLogout() {
    try {
        const response = await fetch('/logout', {
            method: 'POST',
        });

        if (response.ok) {
            // Redirect to index.html after successful logout
            window.location.assign('index');
        } else {
            const message = await response.text();
            document.getElementById('responseMessage').innerText = `Error: ${message}`;
        }
    } catch (error) {
        document.getElementById('responseMessage').innerText = `Error: ${error.message}`;
    }
}





///////////////

