CSC223: Introduction to Software Engineering
Project A4 readme.txt
G2 members: Ali Eshghi, Charlotte Gephart, Emily Kim, Ester Zhao

How to run A4
1. Open Controller class
2. Run program via Controller
	When choosing the option to login as an employee or user, use aliciagrubb as the 
username and password5 as the password (or look through controller-employees.csv or 
controller-users.csv for a more extensive list of viable usernames/passwords).
3. Interact with program via Eclipse console

NOTE: When opening the map multiple times during use, the map may open behind the console instead of in front. If the map does not appear to open when it should, it will be behind the console.

Testing:
How to run tests:
1. Open ValleyBikeSim.java
2. Change flags path and savepath to "test-data-files/" and "test-data-files-junk/" respectively
3. Open ValleyBikeSimTest.java
4. Run the program

NOTE: When testing add station the MapApp will pop up. Tester should click coordinates on the map to confirm add station and continue testing.

We chose to write unit tests for ValleyBikeSim because the user never interacts directly with that class, and thus in order to get a comprehensive idea of what each method should do it was necessary to write tests in the code.

For the controller, which is heavily dependent on user feedback, we used black box testing with other groups as well as repeated testing of as many cases and situations we could think of within our own group.

Coding References: 
1. Baeldung. “Difference Between Wait and Sleep in Java.” Baeldung, 23 Sept. 2019, 
https://www.baeldung.com/java-wait-and-sleep.
2. Doe, John Abel. “How to Draw on Top of an Image in Java?” Stack Overflow, 1 May 1967, https://stackoverflow.com/questions/43081526/how-to-draw-on-top-of-an-image-in-java.
3. ChronoUnit (Java Platform SE 8 ), 11 Sept. 2019, https://docs.oracle.com/javase/8/docs/api/java/time/temporal/ChronoUnit.html.
4. Espiritu, Abraham Miguel EspirituAbraham Miguel. “Java: Empty While Loop.” Stack Overflow, 1 Jan. 1962, https://stackoverflow.com/questions/8409609/java-empty-while-loop/13899264. Gamma et al, Design Patterns, Ch1, 1995, CSC 223 Course Moodle Page
5. “Gitignore Documentation.” Git, https://git-scm.com/docs/gitignore.
6. Gupta, Lokesh. “LocalDate Format() - Convert LocalDate to String.” HowToDoInJava, https://howtodoinjava.com/java/date-time/localdate-format-example/.
7. “How to get current timestamps in java.” mkyong.com, 16 Nov. 2016, https://www.mkyong.com/java/how-to-get-current-timestamps-in-java/.
8. “How to Calculate Difference between Two Dates in Java (In Days).” Javarevisited, https://javarevisited.blogspot.com/2015/07/how-to-find-number-of-days-between-two-dates-in-java.html.
9. “Javadoc.” Wikipedia, Wikimedia Foundation, 7 Sept. 2019, https://en.wikipedia.org/wiki/Javadoc.
10. “Java.lang.Object.wait() Method.” Tutorialspoint, https://www.tutorialspoint.com/java/lang/object_wait.htm.
11. Moustakas, Tasos “Unhandled Exception Type ParseException.” Stack Overflow, 1 Sept. 1962, https://stackoverflow.com/questions/11665195/unhandled-exception-type-parseexception.
12. Rankin, Matthew RankinMatthew. “How Do I Delete a Git Branch Locally and Remotely?” Stack Overflow, 1 Mar. 1960, https://stackoverflow.com/questions/2003505/how-do-i-delete-a-git-branch-locally-and-remotely.
13. “Singleton pattern” Wikipedia, 21 Nov. 2019, https://en.wikipedia.org/wiki/Singleton_pattern.
14. Singh, Chaitanya. “Java 8 - Calculate Days between Two Dates.” Beginnersbook.com, 17 Oct. 2017, https://beginnersbook.com/2017/10/java-8-calculate-days-between-two-dates/.
15. “Wrapper Classes in Java.” GeeksforGeeks, 28 Aug. 2018, https://www.geeksforgeeks.org/wrapper-classes-java/.
16. W10 Notes: Architecture, CSC 223 Course Moodle Page


