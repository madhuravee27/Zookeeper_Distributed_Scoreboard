all: pre run

pre:
	sudo apt-get update	
	sudo apt install default-jdk --assume-yes
	sudo apt-get install ant --assume-yes

run:
	ant



