# trade-instrument-pricer
This project takes prices of instruments traded on venue. It captures the prices, caches and later persists price information provided by vendor.

# DESCRIPTION :
The project accepts prices of the traded instrument from vendors. The vendors can push the prices either from 
* the REST API endpoints or 
* by dropping the csv file in the drop Folder.
    
The drop Folder keep polling for
the files ending with *.csv. Later, it reads the file, transforms the data into equivalent Object and send the messages
to service to be cached and publishing the data to JMS Topic.

Thus, before processing the prices it will cached the data and send it to JMS topic. Incase the prices sent by vendor is
duplicated than it will override the old data with the new entries.

Cache Management will remove the old data after 30 days.

The project is built on Java 11 using maven configuration and Spring boot start up web.
The project on start up reads the default CSV file loads 4 records by default.
http://localhost:8080/api/prices loads the prices.


### High Level FLow:

![High Level Flow](./images/highlevel.png)

## Domain Model
The Pojo consists for 3 main classes: Vendor, Instrument and TradePrice.
TradePriceRequest is used to post the incoming http request, to further get them cached and published in JMS Topic.

![Model](uml-diagram/classDiagram.jpeg)

