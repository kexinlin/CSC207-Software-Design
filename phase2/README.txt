How to run the program

All cash or cheque put into the machine are simulated using `deposits.txt` file.
This includes the bank manager adding cash to the machine.

`records.txt` file contains settings for the program.
Below is the login information of user, bank manager and user-employee.

==========================
* User:

username: test
password: 111

username: klin
password: 123

username: u1
password: xxx

* Bank manager:

username: m1
password: 111

* User-employee:

username: e1
password: 111
==========================

example for cheque transactions:
cheque,1231.56

example for cash transactions:
cash,5 10,20 3,50 6

All cash taken out of the machine are simulated using `withdraw.txt` file.
Its format is the same as `deposits.txt`, but is only used for cash.
