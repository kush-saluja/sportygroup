# SportyGroup Feed Ingestion Service

## Overview
SportyGroup is a Spring Boot-based application designed to process sports betting data feeds from different providers.
The application accepts feed data via REST API endpoints, transforms the data into a standardized format, and processes it asynchronously.
In the scope of this assignment, the messages are logged on console.

## Features
- Accepts feed data from multiple providers (currently Alpha and Beta as specified in requirement)
- Processes two types of events:
  - Odds Change Events: Updates to betting odds
  - Bet Settlement Events: Outcomes of bets
- Transforms provider-specific JSON formats to a standardized internal format
- Asynchronous processing of feed data

## Technologies
- Java 17
- Spring Boot 3.5.3
- Jolt (JSON to JSON transformation library)
- Maven

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven

### Building the Application
1. Clone the repository
2. Navigate to the project directory
3. Build the application using Maven:
   ```
   mvn clean install
   ```

### Running the Application
Run the application using Maven:
```
mvn spring-boot:run
```

The application will start on port 8084 with context path `/sportygroup`.

## API Documentation

### Feed Ingestion Endpoints

#### Alpha Provider Feed
```
POST http://localhost:8084/sportygroup/api/v1/provider-alpha/feed
Content-Type: application/json
```

##### Example Payload for Odds Change:
```json
{
  "msg_type": "odds_update",
  "event_id": "ev123",
  "values": {
    "1": 2.0,
    "X": 3.1,
    "2": 3.8
  }
}
```

##### Example Payload for Bet Settlement:
```json
{
  "msg_type": "settlement",
  "event_id": "ev123",
  "outcome": "1" 
}
```

#### Beta Provider Feed
```
POST http://localhost:8084/sportygroup/api/v1/provider-beta/feed
Content-Type: application/json
```

##### Example Payload for Odds Change:
```json
{
  "type": "ODDS",
  "event_id": "ev456",
  "odds": {
    "home": 1.95,
    "draw": 3.2,
    "away": 4.0
  }
}
```

##### Example Payload for Bet Settlement:
```json
{
  "type": "SETTLEMENT",
  "event_id": "ev456",
  "result": "away" 
}
```

### Response Format

#### Success Response
```json
{
  "message": "Accepted"
}
```

#### Error Response
```json
{
  "message": "Error message details"
}
```

## Usage Examples

### Using cURL

#### Alpha Provider - Odds Change
```bash
curl -X POST http://localhost:8084/sportygroup/api/v1/provider-alpha/feed \
  -H "Content-Type: application/json" \
  -d '{
    "msg_type": "odds_update",
    "event_id": "123456",
    "values": {
      "1": 1.5,
      "X": 3.2,
      "2": 5.0
    }
  }'
```

#### Beta Provider - Bet Settlement
```bash
curl -X POST http://localhost:8084/sportygroup/api/v1/provider-beta/feed \
  -H "Content-Type: application/json" \
  -d '{
    "type": "SETTLEMENT",
    "event_id": "123456",
    "result": "home"
  }'
```

## Data Mapping

The application uses Jolt transformations to map provider-specific
JSON formats to a standardized internal format:

### Alpha Provider
- Odds Change: Maps `msg_type: odds_update` to `ODDS_CHANGE` event type
- Bet Settlement: Maps `msg_type: settlement` to `BET_SETTLEMENT` event type
- Bet Outcomes: Maps `1` to `HOME`, `X` to `DRAW`, `2` to `AWAY`

### Beta Provider
- Odds Change: Maps `type: ODDS` to `ODDS_CHANGE` event type
- Bet Settlement: Maps `type: SETTLEMENT` to `BET_SETTLEMENT` event type
- Bet Outcomes: Maps `home` to `HOME`, `draw` to `DRAW`, `away` to `AWAY`

## Add New Provider

To add any new provider, make sure you follow the following steps : 

- Add a new entry in FeedProvider.Java with the provider path information.
- Add the jolt specification for that provider in resources/templates.
- The name of the template must lowercase(feed provider name)_mapper.json.

