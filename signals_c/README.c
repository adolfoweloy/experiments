# Example of handlers for OS signals

This simple source file was created just to see in practice how each signal processes are handled in unix based systems.

This is good to understand what exactly are each of -9, -15 or other options for kill command.

To check for all available signals for kill command run:
```
kill -l
```

The program created here just handles SIGTERM which can be issued by running:
```
kill -15 <pid>
```

What each signal means can be checked on the official documents, but in short:
- SIGTERM allows the process to handle the termination by cleaning up whatever it needs to gracefully terminate the running process;
- SIGKILL the famous, kill -9, does not allow for the process to handle the termination (which might not be good because it won't give the chance for the running process to terminate child processes as well as clean up temporary files for example);
- SIGINT is the signal when the process is interrupted by a Ctrl+C
- SIGQUIT is the signal when the process is interrupted by using a Ctrl+Q

