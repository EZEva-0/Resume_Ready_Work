#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <dirent.h>
#include <string.h>
#define BUFFSIZE 32768

/*Simple-lscat is a combination of simple-ls and simple-cat
Simple-ls serves as the main, but whenever we reach a .c file, we fork
and use the functionality of simple-cat*/

int main(int argc, char **argv) 
{
	//read files names, ls functionallity
	DIR *dp;
	struct dirent *dirp;

	if (argc != 2) 
	{
		fprintf(stderr, "usage: %s dir_name\n", argv[0]);
		exit(1);
	}

	if ((dp = opendir(argv[1])) == NULL ) 
	{
		fprintf(stderr, "can't open '%s'\n", argv[1]);
		exit(1);
	}

	while ((dirp = readdir(dp)) != NULL )
	{
	    int size = strlen(dirp->d_name);
	    
		printf("%s\n", dirp->d_name);
//-------------------------------------------------		
		//fork here on .c files
		if(dirp->d_name[size-1] == 'c' && dirp->d_name[size-2] == '.')
		{
		    //now fork on child
		    pid_t child_pid = fork();
            int child_status;
		    // Child
            if (child_pid == 0)
            {
                //child code - call cat functionality
                char *args[] = {"cat", dirp->d_name, NULL};
                execvp(args[0], args);
                
                printf("Command not Executed\n");
                exit(0);
            }
            // Parent
            else
            {
                wait(child_pid);
                printf("Resuming main...\n");
            }
		}
	}
	closedir(dp);
	return(0);
}


