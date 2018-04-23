# Events

Web Application that allows for search of events within London.

This web application integrates with the following apis:
*  http://api.eventful.com/ 
*  https://openweathermap.org/api

An api key is required for both of these services


#### To Run The Tests
````
mvn clean test -Devents.api.key={eventfulApiKey}-Dweather.api.key={weatherApiKey}
```` 

#### To Run the Application

````
mvn clean spring-boot:run -Devents.api.key={eventfulApiKey}-Dweather.api.key={weatherApiKey}
```` 

#### A Rest Api will be available to search for events
````
http://localhost:8080/api/events?q=(searchString}&sort=(sortField,asc|desc)&page=(pageNumber)&size=(pageSize)

````

#### Sort Fields
*   name.sort - sort by the name of the event
*   startTime - sort by the start time of the event
*   venue.sort - sort by the venue of the event
*   categories.sort - sort by the category of the event

#### Examples
* http://localhost:8080/api/events?q=food&sort=startTime,asc *Finds all the events for search food and orders by startTime*

* http://localhost:8080/api/events?q=rain&sort=venue.sort,desc *Finds all the events where it will be raining, and orders by venue descending*
