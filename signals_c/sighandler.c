/*
 *Example created using this page as reference: https://www.gnu.org/software/libc/manual/html_node/Termination-in-Handler.html#Termination-in-Handler
 */ 
#include <stdio.h>
#include <signal.h>

void signal_handler(int sig);
volatile sig_atomic_t term_in_progress = 0;

int main(int argc, char** argv)
{
    // setup to stop buffering stdout
    setvbuf(stdout, NULL, _IONBF, 0);
    // registers signal_handler as the handler for SIGTERM (-15) signal
    signal(SIGTERM, signal_handler);

    while (1) {
        // executing...
    }
}

void signal_handler(int sig)
{
    if (term_in_progress) 
        raise(sig);
    term_in_progress = 1;

    printf("\ncleaning up...");
    signal(sig, SIG_DFL);
    raise(sig);
}

