# README

## Stars
### Design details
There are two packages in this project edu.brown.cs.lsim2.kdtree which holds my KDTree data structure, and edu.brown.cs.lsim2.stars that holds my CSVReader, REPL, and all the Command class needed for the functionality of stars. 

My KDTree is generic, so that it can hold KDTreeNodes that hold any object that implement the Cartesian interface. The Cartesian interface is basically where the class has a list of coordinates for it's point. I also have an Entry class, that is used to store data in my PriorityQueue. This is to make it easier to keep track of points that are close to the query point as well as their distances to the query point (for the PriorityQueue to be easily sorted). 

In my Stars class, I have a REPL that would keep reading command line input until EOF or CTRL-D is entered. I also have a Command interface, which is implemented by the Read, Neighbors and Radius class. These Command class represent the 3 commands users can type: "stars", "neighbors" and "radius". They implement the Command interface so they can be generically called in the REPL without the REPL actually knowing what command was called. These Commands are stored in a 'Functions' hashMap<String, Command>, where the user input string is mapped to the command it has to execute. This makes it easier to add more commands later on, without needing a string of if-else statements. 

When the REPL parses the string input, it will look up the corresponding command in the 'Functions' hashMap and tell that corresponding Command class to execute it's task. If it's a Read class, the Read class will call the CSVReader to read the file and parse the data into a list of Stars and return that list to the REPL. If it's a Neighbors or Radius class, it will call on their corresponding search functions and return the search results to the REPL. The REPL then prints out the results to the terminal. The REPL is the only class that actually prints to the terminal. If any errors occur when running the inner functions, an exception will be thrown instead, to be caught by the REPL class and printed to the terminal. This makes the code more structured, and if we were to use the other functions in the future for non-terminal printing purposes, it would be easier to extend from that. 

The GUI is launched from the Main method and will use the same data set as the one on the CLI. This means, if a new data set was loaded into the CLI, the GUI will also update it's data set, because they are both using the same reference to the data set. The SubmitHandler class handles what happens when the user submits a search request, and returns the results to the GUI. 


### How to run any tests you wrote/tried by hand
Type 'mvn test' to run all JUNIT tests. Please note that the KDTree integration tests may take some time to run (15-20 seconds) because the input is randomly generated and the operation is run in a loop multiple times while the output is compared against a naive search algorithm. 

### To run the system tests run: 
./cs32-test ./tests/ta/stars/* 
for the TA test suite and 
./cs32-test ./tests/student/stars/*
for my test suite 

#### NOTE: 
there is only a 50% coverage for testing in the stars package because I wasn't sure how to test the front end methods and handlers using JUNIT tests. The REPL is also half tested with JUNIT tests, because the remaining of the functionality was tested using System Tests. 

### How to build/run the program: 
To build the program: run 'mvn package' in the the file directory holding the files (ie. 'projects-lsim2').
To run the CLI: type './run' 
To run the GUI with the CLI: type './run --gui --port 4567' and open in the web browser: 'http://localhost:4567/stars'
Make sure to load the data through the CLI before interacting with the GUI!

#### NOTE: 
Since the KDTree integration tests are random, if the KDTree integration test fails (it means there was an edge case I didn't manage to catch :'( ), it might cause the build to fail. If so, you can comment out the KDTree integration test or run 'mvn package' again, so that the project actually builds. Hopefully this doesn't happen, as I've ran tests multiple times and it hasn't failed yet, but just letting you know it advance. 

### Design questions for Stars:

1. In my design, all commands are classes that implement the "Command" interface, and in the REPL, they are added to a functions hashMap that maps the string commands to an instance of the Command class. All Command classes must implement the executeTask() interface, which performs the command they are supposed to. This makes it easier to extend the code to more commands as I would need to just make a new class for the third command and fill in the executeTask() method to specify what it's supposed to do. Then in the REPL, I would add a new Key, Value pair for the third command in a String, and an instance of that third command's class. 

2. Points on the earth's surface are only 2 dimensional (longitude and latitude), however, since the earth is a sphere, it would be inaccurate to transform points on a sphere into a 2D plane. It would be more difficult to calculate for distances between points since you can't use the Euclidean distance. The longitude also wraps at a certain point from -180 to 180, so we might either need a different way to represent points on the earth or find another way to calculate distance between two points.  


3. I would need to add all the 15 methods required by the Collection interface, as currently my KDTree doesn't support some of these methods. An easier way to do this would also be to get my KDTree class to inherit from AbstractCollection class, which would create most of the methods for my KDTree and the only methods I would have to add are the size() and the iterator methods. The iterator method might be more difficult to implement because I would need to keep track of both branches of the tree, and the traversal isn't exactly linear. I do have a list of stars within my KDTree class as ArrayList<Star>, so I could just iterate over that, but the problem would be the ArrayList<Star> isn't sorted in the same structure of the KDTree. So I think the best way would be to do a tree traversal (perhaps a non-recursive pre-Order traversal) to implement the next() and hasNext() methods. 

### Explanations for any Checkstyle errors your code has: 
There is a FindBugs error from the Main.java class, but it's from the stencil code. Also, since I imported the new pom.xml for the coverage checking, there are some warnings in the stencil Main.java class as well. 

### Known Bugs
The system test might timeout sometimes, but usually runs properly on the second try. I can't seem to figure it out. 

## Autocorrect
### Design details
There are 3 packages for this project, the CLI package that handles the CLI + GUI and handling higher level methods, the Trie package, which only deals with the Trie data structure, and the Autocorrect package, which contains the project specific command classes. 

### Autocorrect package:
As with the Stars project, I have a Command interface that is implemented by all commands required in the project. In this project particularly, there is the Corpus command, which reads from a file and parses the information into a trie as well as calculated bigram and unigram counts. The Suggest command, tells the trie to output suggestions based on the suggestion generation options (PREFIX,WHITESPACE etc.) provided by the user. These four options PREFIX, WHITESPACE, LED and SMART are represente as enums that have values denoting whether they are switched on/off. Finally, the Ranker class takes in the entire list of raw suggestions, and ranks them according to the specification. First finds an exact match, then compares it using the bigram, unigram and alphabetically. If the smart option is turned on, the ranker also uses the smart ranking technique (as described below). 

### CLI package
This handles running the program in the CLI and the GUI, thus it contains all the hanlders required for the GUI to work. It also contains the REPL class, which is where most of the action happens and is delegated. (Refer to the Stars README for more details on how the REPL functions). The only main difference with the new REPL is that it now stores a lot more information and most of the data structures live in the REPL, since all classes implementing the Command interface must take in the Repl as a parameter. This makes it so that if new commands are instantiated and garbage collected, the data still remains intact, and we can build the trie as we load more corpora into the CLI, instead of overwritting it. 

### Trie package
This is package simply holds the trie data structure. The tire is able to insert words as well as perform the four following functions: 
1. Find: finds if there is an exact match
2. Suggest: suggest words based on the given prefix
3. LED(int maxDist): finds words in the trie that are within the given LED
4. Whitespace: finds words that can be legally split into two

### How to run any tests you wrote/tried by hand
Type 'mvn test' to run all JUNIT tests. Please note that the KDTree integration tests from stars may take some time to run (15-20 seconds).

### To run the system tests run: 
./cs32-test ./tests/ta/autocorrect/* 
for the TA test suite and 
./cs32-test ./tests/student/autocorrect/*
for my test suite 

### How to build/run the program: 
To build the program: run 'mvn package' in the the file directory holding the files (ie. 'projects-lsim2').
To run the CLI: type './run' 
To run the GUI with the CLI: type './run --gui --port 4567' and open in the web browser: 'http://localhost:4567/autocorrect'
Make sure to load the data through the CLI and turn on prefix/whitespace/led before interacting with the GUI!

### Smart Suggestion:
The smart ranker smartly ranks the suggestions based on how many times the words have appeared in previous searches, which means if a word 'hello' has apppeared in the user's suggestion results multiple times, it is more likely to appear in the top five suggestions later on. This is similar to some search engines, where they keep track of your search habits and give autocomplete suggestions based on that. In the implementation, the helper method smartWeight calculates the count and weights its 'previous score' based on how many times the user has conducted an autocorrect search. This implies that the search gets 'smarter' on frequent use, as they have more data to work on. The smart ranker essentially starts out the same as the normal ranker, and gets smarter over time. This is a slightly smarter way of ranking words compared to the given suggestion, as it will also keep track frequency of use in real time, as opposed to simply referring to a static corpus. One way I think would be an improvement to this (if we think in terms of the GUI, is if the user clicks on a given suggestion, it the suggested word would have an even higher score later on). 

### Design questions:
1. With the way my data structures are implemented I would have sets of data structures (tries, bigram counts etc.) for the above specification to work, since all new corpora is always loaded into the same trie in my current CLI. I might have to think about thread safety as well, if both queries from two different inputs are done concorrently. For two different inputs on the same page, I might have the same handler, but have to modify the frontend to have a different set of fields which correspnd to my SuggestHandler, so the suggestions run on different tries. If they are two different pages, I might need two handlers, and also modify the backend code so it's more extensible to add different corpora to different pages (e.g. have a hashmap that maps from the input field to the corresponding data structures). 
2. In my implementation of the trie, if a trieNode containing a letter is also the end of a word (e.g. it's letter 'd' in the entry 'word'), it will know so. With how the corpus is processed and it is passed into the construction of the trie, it would result in only adding only more node to every single word end, since the first n-1 letters in the theta-appended word would be the same as the normal word. Therefore, it would simply keep traversing down the trie, matching it with the same nodes until it reaches the last letter. When it reaches the last letter, the last letter's node will add a child containing theta, and the theta node will mark itself as 'word end' as well. So with every word end node, there will be a child containg the theta letter appended to it. 

## Bacon
This project added two new packages:
### Graph package
This graph package has a generic Edge, Node and Path, which must be subclassed if they want to be used. (For example, my Actor and Pass classes have to extend from the Node and Edge class accordingly). I also have a Dijkstra's class that is very generic, so it can be used on other types of nodes and edges as well (as tested in the GraphTest file). This is so that when I need to use this algorithm later, I simply need to make new subclasses of these generic classes and not worry about the dijkstra's algorithm breaking. The Path class basically connects one Node to another and stores the edge values between them, it's used when processing the output of the algorithm. 

### Bacon package
This package contains a Database class, which is the only class that directly interacts with the SQL server. There are two main data types, Actors and Movies. These two types have their own proxies and beans. When an Actor/Movie is instatiated, it gives it's proxy it's ID, but doesn't fill in any information yet. Only when the information is needed, the proxy goes to the database and retrives all information at once and fills the bean either from the cache (if it was stored) or from the SQL database itself. 

When filling the movie bean, the getMovieSet() method is called, which returns the set of movies the actor has starred in and also partially constructs the graph for dijkstra's. On every call of getNeighbors() in the Dijkstra's algorithm, this method will get all the movies and legal actors associated with the current actor node. A join query is used to speed up the process so we don't have to make as many queries, and can immediately construct the graph without looping over all movies and looping over all actors unnecessarily. This is so that not all the information is loaded into the graph at once, and we build the graph dynamically as the dijkstra's algorithm progresses. This was done in an attempt to make my Dijsktra class as generic as possible, so it doesn't need to worry if the initials match (all of these will be handled by the Actor and Database methods themselves). 

The proxies also implement the Entity interface so that the query methods could be more generic. 

I've also added two additional "Command" classes (read above to find out more about the Command interface). The Load class is called when the user loads a new database into the program using the 'mdb' command. The Connect class is called when the user uses the 'connect' command to connect two actors.  
### CLI additions
I've added some SparkHandlers for the GUI, including BaconHandler (to handle autocorrect suggestions), BaconResHandler (to handle generating results) and ListHandler (to generate infinite pages). 

### Runtime optimizations
1. Using a loading cache and storing anything that I've queried
2. Checking if nodes are visited in dijkstra's as an optimization for the algorithm
3. Using a JOIN when querying the database so that I get all the movies and actors with the correct first initial for a particular actor at one go. 
4. I use a static connection and preparedStatement and close prep and the resultSet after every query. 

### How to build and run your program from the command line
To build the program run 'mvn package'. To run the CLI, type ./run. To run the GUI, type ./run --gui and load the database (the autocorrect functionality will load in as well). If you are typing very fast in the input boxes, the program will wait 500ms before showing the autocorrect results, this is because the dataset might be very large and it will slow down the GUI when giving autocomplete suggestions. 

Link to bacon page: 'http://localhost:4567/bacon' 

Note: Please ctrl-D before ctrl-C when quitting, it might not cause problems, but just to make sure, the database is closed properly. 

### How to run your tests
The JUNIT tests are run when the program is built. The system tests are separated into two: 
1. the tests on the small database, to run this, simply type: ./cs32-test tests/student/bacon/* 
2. before testing on the larger database, please create a directory /ltmp/<login> and copy over the bacon database. Then you might need to change the file path on the large database test files in baconLarge to your specific directory, since mine runs using my login. (Sorry for the trouble!). To run the test on the large database: ./cs32-test tests/student/baconLarge/* -t 60

### Any tests you wrote and tried by hand
Most tests are quite generic and try to test as many methods as possible. Since my dijkstra's is generic, I also had tests testing the generic implementation of dijkstra's with no relation to actors or movies. Another test that is significant is also testing multiple queries in a go, to make sure the cache works and that caching does not hinder the accuracy of the search (e.g. connect_twice.test, where I try to connect the same starting actor with two different actors and vice versa without quitting the program). 

### Design questions and answers
1. The other developers can simply write a new class with the new graph search algorithm and place it into my graph package. Then if they want to use that new graph search, they simply need to call it in my Connect class and instead of instantiating a new Dijkstra's class to perform the search, they need to instantiate their own graph search class. In order to remove any other constraints, I tried making my main Dijkstra class as generic as possible, so it doesn't need to worry about whether the initials match, all these are handled by the actor classes, therefore it wouldn't affect the new algorithm. 

2. Since I already have a CSV reader in place, I might also need to make a txt file reader etc. depending on what other files I need. To improve my code, my Load class will then have to be able to use all these other readers to load information into my program. So if a SQL server is not used, I might need to either have a separate efficient data structure (not such a good idea if it's a very large dataset), or find a way to look up data from my non-sql files efficiently (perhaps transfer them into a SQL database). Caching would also play an important role, so it's less expensive when looking up data. 

3. I might either need to change the way the weights are calculated so that the year a movie was released is taken into consideration when performing the dijkstra's algorithm, or when creating the path for transferring the bacon, I would have to write a comparator in order to sort the chain of movies in chronological order. 

### Known bugs
Occasionally when running the gui, I get a SIGSEV error stemming from my ./run file. I've tried to fix it but can't seem to pinpoint what's wrong. If this occurs, try to mvn clean and rebuild the project again.

### Possible swag?
1. Added a visual graph to show how the bacon is passed from one actor to the next with *fun* pictures! 
2. Buttons to re-navigate to other pages
