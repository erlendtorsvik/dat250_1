# Technical problems
When checking the SHA-256 checksum of my installation package, i first got a "False" comparison between my MongoDB installer hash and the signature file.
This was because i had the SHA256 signature of the 4.4.0 version that the tutorial provided, while having the 4.4.1 version of the installer.
After getting the signature for the right version, i got a "True" comparison, and the package was validated. 

# Validation Screenshot
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/VALIDATION.PNG)

# Experiment 1

## Insert
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/Insert.PNG)

## Query
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/Query.PNG)

## Update
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/Update.PNG)

## Delete
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/delete.PNG)

## Bulk write 
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/bulkwrite.PNG)

# Experiment 2

## Example 1
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/exp2example1.PNG)

## Example 2
![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/exp2example2.PNG)

## Additional operation
My operation connects specific days with specific costumers. This enables you to see all the costumers that have visited on specific days, and if they have visited more than once in the span of one day.



![](https://github.com/erlendtorsvik/dat250_1/blob/master/assignment3/images3/Function.PNG)

