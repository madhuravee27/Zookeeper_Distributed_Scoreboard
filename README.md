# ZooKeeper_Distributed_Scoreboard
A distributed scoreboard is implemented that shows player scores for a game using Zookeeper. SA Zookeeper protocol is built to maintain two score lists, one contains the N most recent games and the other maintains the N highest scores.

Two programs are built: watcher and player. A watcher process displays the two lists and updates the list in real time as necessary. A player process does one of three actions: join, leave, or post a score.

To install dependencies(Java 8, Apache Ant):
* Run Makefile using 'make' command.

A Zookeeper server is to be started.

To run watcher:
* ./run.sh watcher [ip address:port number] [count]

To run player:
* ./run.sh player [ip address:port number] [name] [count] [delay] [score]
* ./run.sh player [ip address:port number] [name]
* ./run.sh player [ip address:port number] ["first last"]
