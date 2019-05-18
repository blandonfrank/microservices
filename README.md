# Microservices

This will be a list of projects/services used to have a full end-to-end simulation of stocks getting traded.

### Trader service
The trader service is at the heart of the entire application flow. This service gets called from the UI layer whenever a user decides to BUY a new stock - given that they have the funds -, or decides to SELL a current holding.
######   Features
* Buy Stocks
* Sell stocks

###### Tech
*   [Spring Boot](https://spring.io/projects/spring-boot) - framework
*   [RabbitMQ](https://www.rabbitmq.com/)- for messaging
*   [Eureka](https://github.com/Netflix/eureka) - for service registry
*   [Zuul](https://github.com/Netflix/zuul) - for routing
*   [Sleuth](https://cloud.spring.io/spring-cloud-sleuth) - for tracing

### Portfolio service
Keep track of the portfolio assigned to an owner/username
The porfolio service gets updated whenever the user buys or sells a stock. It keeps track of the user's cash balance, current stock holdings.
######   Features
* Withdraw cash
* Deposit Cash
### Risk analyser service
The risk analyzer service will use the portfolio service to get a list of current holdings for all portfolios, and run those holdings against a ML model to determine the risk level of the portfolio,
and give the portfolio the appropiate rating and also rate each individual stock.
######   Features
* Analyze individual stocks using historical data from Yahoo Finance to determine whether the the stock needs to be upgraded to a BUY, downgraded to a SELL, or to HOLD
* Rates a portfolio holdings to a low, medium, or high risk based off the current individual stock holdings.

###### Tech
*  [Spring Data XD](https://projects.spring.io/spring-xd/) - for data transformation
*  [Apache Geode](https://geode.apache.org/) - for storage
*  [Spark](https://spark.apache.org/) - for analytics
*  [R](https://www.r-project.org/) - For the ML model code
### Forecasting/projection service
######   Features
### Web application Service
The web application will allow an authenticated user to BUY or SELL a stock, it will also show them the current rating of said stock, and shows them their portfolio balance if they have one.
######   Features
* Allows users to BUY or SELL stocks
* Allows a user to see their portfolio balance
* Displays a stock rating - BUY, SELL, or HOLD
* Displays a user's portfolio's risk rating

###### Tech
* [React](https://reactjs.org/)
  


### Development

Want to contribute? Great!

Send me a message!

### Todos

 - Too many things to list at this time

License
----
