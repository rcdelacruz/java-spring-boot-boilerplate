// This script adds environment-specific styling to the Swagger UI
window.onload = function() {
    // Wait for Swagger UI to fully load
    const checkExist = setInterval(function() {
        if (document.querySelector('.swagger-ui .info .title')) {
            clearInterval(checkExist);
            customizeSwaggerUI();
        }
    }, 100);
    
    function customizeSwaggerUI() {
        // Get the title element which contains our environment info
        const titleElement = document.querySelector('.swagger-ui .info .title');
        const titleText = titleElement.textContent;
        
        // Extract environment from title (e.g., "Spring Boot Training API - PRODUCTION")
        let environment = 'default';
        let headerColor = '#1b1b1b'; // Default dark color
        
        if (titleText.includes('PRODUCTION')) {
            environment = 'production';
            headerColor = '#e74c3c'; // Red
            
            // Add a production warning banner
            const banner = document.createElement('div');
            banner.style.backgroundColor = '#e74c3c';
            banner.style.color = 'white';
            banner.style.padding = '10px';
            banner.style.textAlign = 'center';
            banner.style.fontWeight = 'bold';
            banner.style.fontSize = '14px';
            banner.innerHTML = '⚠️ PRODUCTION ENVIRONMENT - Be careful with any API calls you make here! ⚠️';
            
            // Insert at the top of the page
            const topBar = document.querySelector('.swagger-ui');
            topBar.insertBefore(banner, topBar.firstChild);
            
        } else if (titleText.includes('DEVELOPMENT')) {
            environment = 'development';
            headerColor = '#2ecc71'; // Green
        } else if (titleText.includes('TEST')) {
            environment = 'test';
            headerColor = '#f39c12'; // Orange
        } else if (titleText.includes('LOCAL')) {
            environment = 'local';
            headerColor = '#3498db'; // Blue
        }
        
        // Add a custom style element to the head
        const style = document.createElement('style');
        style.textContent = `
            /* Environment-specific styling */
            .swagger-ui .topbar { 
                background-color: ${headerColor} !important; 
            }
            
            /* Add a subtle border at the bottom of the header with the environment color */
            .swagger-ui .information-container {
                border-bottom: 5px solid ${headerColor};
                padding-bottom: 20px;
            }
            
            /* Make the environment badge in the description stand out more */
            .swagger-ui .info .title {
                position: relative;
            }
            
            /* Add environment indicator to favicon */
            .favicon-badge {
                position: absolute;
                bottom: -5px;
                right: -5px;
                width: 15px;
                height: 15px;
                background-color: ${headerColor};
                border-radius: 50%;
                border: 2px solid white;
            }
        `;
        
        document.head.appendChild(style);
        
        // Add a data attribute to the body for potential future styling
        document.body.setAttribute('data-environment', environment);
    }
};
