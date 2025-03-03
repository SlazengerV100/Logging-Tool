# Stats Servlet

This program is a small client-server interaction with a series of servlets that can create logs via POST requests. The program also has endpoints for GET requests to retrieve these logs in either HTML table, CSV or Excel formats.

# Running
Run using `mvn jetty:run`

To stop, open another terminal and use `mvn jetty:stop`

To use the client, run: `java -cp <your-class-path> <csv|excel> <fileName>`

Note: the type (csv or excel) is **case insensitive**.
