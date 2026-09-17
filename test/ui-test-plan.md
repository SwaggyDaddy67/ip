# UI Test Plan

Each test case runs GLaDOS with a fixed sequence of typed commands (the
**Input**) and checks the full console output against the **Expected
output** block, character for character (line-ending differences between
Windows and Unix are ignored).

Each test runs in a fresh, empty folder, so it starts with no saved tasks.
A test can optionally add a **Data file before:** block (written to
`data/glados.txt` before the run, to test loading) and a **Data file
after:** block (the exact contents `data/glados.txt` must have once the run
ends, to test saving).

Update this file whenever a command's input format or reply wording
changes, so the test-ui skill keeps checking against current behaviour.

## Test 1: Add a todo, deadline, and event, then mark and unmark

**Aim:** Verify that all three task types can be added, that `list` shows
each one formatted with its type letter and status box, and that `mark`
and `unmark` both update the status shown afterwards. Also verify that the
saved data file holds every task, in order, with its latest done status.

**Input:**
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
list
unmark 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

## Test 2: Reject a todo with no description

**Aim:** Verify that a bare `todo` command is rejected with an error message
instead of adding a blank task, and that a normal todo entered afterwards is
still added correctly.

**Input:**
```text
todo
todo read book
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     A todo with no description. Try again, with words this time.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 3: Reject an unrecognised command, then keep working normally

**Aim:** Verify that an unrecognised command and an empty todo are each
rejected with their own error message, and that a valid todo entered
afterwards is still added correctly, so a rejected command leaves the task
list untouched.

**Input:**
```text
blah
todo
todo read book
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     I have no idea what that was. Try one of: list, todo, deadline, event, mark, unmark, delete, bye.
    ____________________________________________________________
    ____________________________________________________________
     A todo with no description. Try again, with words this time.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 4: Reject invalid mark arguments, then mark successfully

**Aim:** Verify that `mark` with no number, a non-numeric argument, and an
out-of-range number are each rejected with their own message rather than
crashing, and that a valid `mark` still works afterwards and is saved as
done (`1`) in the data file.

**Input:**
```text
todo read book
mark
mark abc
mark 999
mark 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Which task? Give me a number, like mark 2.
    ____________________________________________________________
    ____________________________________________________________
     "abc" is not a task number.
    ____________________________________________________________
    ____________________________________________________________
     There is no task 999. Your list has 1.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 1 | read book
```

## Test 5: Reject malformed deadline and event input

**Aim:** Verify that a deadline missing its `/by` and an event missing its
`/to` are each rejected with a message naming the correct format, rather
than crashing, and that a well-formed deadline entered afterwards is still
added correctly.

**Input:**
```text
deadline return book
event meeting /from Mon
deadline return book /by Sunday
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     A deadline needs a /by. Try: deadline return book /by Sunday.
    ____________________________________________________________
    ____________________________________________________________
     An event needs both a /from and a /to. Try: event meeting /from Mon 2pm /to 4pm.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] return book (by: Sunday)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 6: Delete tasks, including invalid delete arguments

**Aim:** Verify that `delete` with no number, a non-numeric argument, and an
out-of-range number are each rejected, that deleting a middle task removes
it and shifts later tasks up in `list`, and that deleting the remaining
tasks leaves an empty list, with the saved data file emptied to match.

**Input:**
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
delete
delete abc
delete 5
delete 2
list
delete 1
delete 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Which task? Give me a number, like delete 2.
    ____________________________________________________________
    ____________________________________________________________
     "abc" is not a task number.
    ____________________________________________________________
    ____________________________________________________________
     There is no task 5. Your list has 3.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 0 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
```

## Test 7: Load saved tasks at startup, then keep saving changes

**Aim:** Verify that tasks in an existing data file are loaded at startup
with their type, details, and done status intact, and that later changes
(unmarking a loaded task, adding a new one) are saved back to the file.

**Data file before:**
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

**Input:**
```text
list
unmark 1
todo join sports club
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 4 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     4.[T][ ] join sports club
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 0 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
T | 0 | join sports club
```

## Test 8: Skip corrupted lines in the data file

**Aim:** Verify that lines in the data file with an unknown type letter, a
missing field, an invalid done flag, or an empty description, and lines that
are not tasks at all, are skipped and reported, while blank lines are
ignored and valid lines still load. Also verify that the next save rewrites
the file with only the valid tasks.

**Data file before:**
```text
T | 1 | read book
X | 0 | unknown type
D | 0 | missing date
E | 2 | bad flag | Mon | Tue
T | 0 | 

not a task at all
D | 0 | return book | June 6th
```

**Input:**
```text
list
todo join sports club
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Your save file is damaged. I skipped 5 unreadable line(s).
     They will be gone for good the next time I save.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
T | 1 | read book
D | 0 | return book | June 6th
T | 0 | join sports club
```

## Test 9: Reject the reserved | character in new tasks

**Aim:** Verify that a todo, deadline, or event containing `|` (the data
file's field separator) is rejected rather than saved in a form that could
not be loaded back, and that nothing is written to the data file.

**Input:**
```text
todo read | book
deadline return book /by June|6th
event meeting /from Mon /to Tue|Wed
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     The | character is reserved for my records. Leave it out.
    ____________________________________________________________
    ____________________________________________________________
     The | character is reserved for my records. Leave it out.
    ____________________________________________________________
    ____________________________________________________________
     The | character is reserved for my records. Leave it out.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

**Data file after:**
```text
(data file does not exist)
```
