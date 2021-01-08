/*
 *  shell1.c 
 *  simplest shell. 
 *     running in loop to read input string (command) to be processed
 *     To exit, type EOF (ctlr+D) or ctlr+C 
 */
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sysexits.h>
#include <unistd.h>
#include <stdbool.h>

char *getinput(char *buffer, size_t buflen) 
{
	printf("$$ ");
	return fgets(buffer, buflen, stdin);
}

int main(int argc, char **argv) 
{
	char buf[1024];
	pid_t pid;
	int status;
    bool exitFlag = false;
	while (getinput(buf, sizeof(buf))) 
	{
		buf[strlen(buf) - 1] = '\0';
        
        
        // my if statmemnt... user input == exit, exit.
	    if(strcmp(buf, "exit") == 0)
	    {
            printf("value of buff: %s\n", buf);
            exitFlag = true;
            exit(0);
	    } 
        
		if((pid=fork()) == -1) 
		{
			fprintf(stderr, "shell: can't fork: %s\n", strerror(errno));
			continue;
		}
		
		else if (pid == 0) 
		{
			/* child */
			execlp(buf, buf, (char *)0);
			fprintf(stderr, "shell: couldn't exec %s: %s\n", buf, strerror(errno));	
			exit(EX_DATAERR);
		}
		
		if(exitFlag)
		{
		    exit(0);
		}
		if ((pid=waitpid(pid, &status, 0)) < 0)
		{
			fprintf(stderr, "shell: waitpid error: %s\n", strerror(errno));
		}
	}
	exit(EX_OK);
}