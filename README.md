# Travel Planner and Itinerary Builder Backend

This is the backend service for the Travel Planner and Itinerary Builder project, designed to assist users in planning trips by fetching real-time data from multiple external APIs.

## Features

- **Authentication**: JWT-based authentication for user sign-up and login.
- **Flight Search**: Fetch flight details using the Amadeus API.
- **Hotel Search**: Get hotel recommendations and details via the TripAdvisor API.
- **Event Search**: Retrieve event details using the Ticketmaster API.
- **Place Finder**: Search for places such as cafes, gyms, and restaurants using Foursquare.
- **Weather Information**: Get real-time weather data through the OpenWeatherMap API.
- **Chatbot**: Generate recommendations for places to visit using the OpenAI API.

## API Endpoints

### Authentication

- `POST /api/auth/signin`: User login.
    - Request Body: `{ "email": "user@example.com", "password": "your_password" }`
- `POST /api/auth/signup`: User registration.
    - Request Body: `{ "username": "John", "country": "USA", "email": "user@example.com", "password": "your_password" }`
- `POST /api/auth/refresh`: Refresh JWT tokens.

### Flight Search

- `GET /api/v1/flights/search`: Fetch flight details using the Amadeus API.
    - Request Parameters:
      ```json
      {
        "origin": "SYD",
        "destination": "BKK",
        "departureDate": "2024-11-01",
        "adults": 2,
        "max": 2
      }
      ```

### Hotel Search

- `GET /api/v1/hotels/search`: Fetch hotel details using the TripAdvisor API.
    - Request Parameters: `{ "location": "Matara" }`

### Event Search

- `GET /api/v1/events/search`: Fetch event details using the Ticketmaster API.
    - Request Parameters: `{ "location": "New York" }`

### Place Finder

- `POST /api/v1/recommendations/getRecommendations`: Fetch recommended places using the Foursquare API.
    - Request Body: `{ "location": "Matara", "placeType": "cafe" }`

### Weather Information

- `GET /api/v1/weather`: Fetch real-time weather data using OpenWeatherMap.
    - Request Parameters: `{ "location": "Colombo" }`

### Chatbot Recommendations

- `POST /api/v1/chatbot/recommend`: Generate travel suggestions using the OpenAI API.
    - Request Body:
      ```json
      {
        "origin": "Matara",
        "destination": "Hambantota"
      }
      ```

## Technologies Used

- **Spring Boot** for the backend framework.
- **JWT** for authentication.
- **Amadeus API** for flight details.
- **TripAdvisor API** for hotel recommendations.
- **Ticketmaster API** for event information.
- **Foursquare API** for place recommendations.
- **OpenWeatherMap API** for weather data.
- **OpenAI API** for chatbot-based travel recommendations.

## Getting Started

### Prerequisites

- Java 17
- Maven
- Access to the external APIs (Amadeus, TripAdvisor, etc.)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/travel-planner-backend.git
2. Navigate to the project directory:
   ```bash
   cd Travel-Planner
3. Install dependencies:
   ```bash 
   mvn clean install
   
4. Configure your API keys in application.properties:
   ```properties
   amadeus.api.key=your_amadeus_api_key
   tripadvisor.api.key=your_tripadvisor_api_key
   ticketmaster.api.key=your_ticketmaster_api_key
   foursquare.api.key=your_foursquare_api_key
   openweathermap.api.key=your_openweathermap_api_key
   openai.api.key=your_openai_api_key
5. Run the application:
    ```bash

    mvn spring-boot:run
6. API Documentation
   
   
   For detailed API documentation, 

https://docs.google.com/document/d/1Nbui_0AeKLJjb5UWv-BfW34vyCyq-kcacQUtC4VynDw/edit?usp=sharing