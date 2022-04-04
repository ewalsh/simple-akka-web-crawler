# simple--web-crawler
A simple web crawler

This is a simple web crawler program to calculate page rank for
the initial url or comma separated list of urls using all of the
hrefs coming from <a> tags contained on a web page to form the next
list of urls to crawl over.

This process continues until a maxium depth is reached.

The program can most easily be run using SBT using the following code
below as a guide.

`sbt "run https://google.com 3"`

or for starting with a multiple url list

`sbt "run https://google.com,https://microsoft.com,https://www.scala-sbt.org/ 3"`

finally, results can be saved in a file using:
`sbt "run https://google.com 3" > output.tsv`

# NOTES FOR FUTURE DEVELOPMENT
After starting this project I quickly realized that I am blocking with
the current recursive loop setup. I started to bring in Akka in an
attempt to set this up in a non-blocking parallel version.

I have made a good start here, but have commented out the code for
future development.
