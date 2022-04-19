# Perfect_Hashing_in_Java

In this project I built a perfect hash table in Java. The task was to read in a text file consisting of just under 16,000 US cities with their corresponding latitude and longitude coordinates. There is a CityTable class that has a constructor which has a parameter for primary table size. It builds the table by reading in the data and hashing into a primary table where collisions are going to take place. On the second pass, I iterated over the primary table and any bucket where there were collisions I created a secondary table by finding a hash function in which no collisions were going to take place. 


After building the table some statistics were performed and outputted to the console. Also the program prompts the user to enter a city and state and uses a find function to check the table for they key in constant time. If the city is located it then outputs the google maps URL to the console and the user can use the link to look at the location in Google Maps.
