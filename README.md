# Lab4-Paging
In this lab you will simulate demand paging and see how the number of page faults depends on page size, program size, replacement algorithm, and job mix (job mix is defined below and includes locality and multiprogramming level).
<pre>
<b>Possible inputs</b>:
10 10 20 1 10 lru 0
10 10 10 1 100 lru 0
10 10 10 2 10 lru 0
20 10 10 2 10 lru 0
20 10 10 2 10 random 0
20 10 10 2 10 lifo 0
20 10 10 3 10 lru 0
20 10 10 3 10 lifo 0
20 10 10 4 10 lru 0
20 10 10 4 10 random 0
90 10 40 4 100 lru 0
40 10 90 1 100 lru 0
40 10 90 1 100 lifo 0
800 40 400 4 5000 lru 0
10 5 30 4 3 random 0
1000 40 400 4 5000 lifo 0
<br>
<br>
To run this program do the following:
1) Go to terminal and go to the directory where this project
   was saved. Go to-> Paging/src
2) In here run-> javac pagingDriver/Pager.java
3) And then run-> java pagingDriver.Pager _one_of_possible_outputs_
4) ex: java pagingDriver.Pager 10 10 20 1 10 lru 0
5) And voila, you ran my program!!
<br>
Issues: If it says it can't find a random number file, please navigate to
        Pager.java in the project and change input to the absolute path
        of your random number file (line 26). If you have any other issues 
        running the program please contact me at jv1019@nyu.edu.
</pre?


