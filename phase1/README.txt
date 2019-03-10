How to run the program

Copy `records.example.txt` to `records.txt`. This file contains very basic
settings for the program. One user, whose username is `u1` and password `xxx`,
and one bank manager, whose username is `mgr1` and password `lolol` will be
load into the program. Once you start the program, the information for all
available commands can displayed through `help` command.

All cash or cheque put into the machine are simulated using `deposits.txt` file.
This includes the bank manager adding cash to the machine.

example for cheque transactions:
cheque,1231.56

example for cash transactions:
cash,5 10,20 3,50 6

All cash taken out of the machine are simulated using `withdraw.txt` file.
Its format is the same as `deposits.txt`, but is only used for cash.
