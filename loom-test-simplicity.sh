#!/bin/sh

benchmark () {
   RESULT=`wrk -t$1 -c$1 -d10s http://localhost:$2/$3 | grep Requests | awk '{print $2}'`
   if [ $4 -ne 1 ]; then
     echo $1 $2-$3 $RESULT
   fi
}

#for CONCURRENCY in 1 2 4 8 16 32
for CONCURRENCY in 32
do
  for PORT in 8080 8081
  do
    #for RUN in 1 2 3 4 5 6 7 8 9 10 11
    for RUN in 1 2
    do
      if [ $PORT -eq 8080 ]; then
        benchmark $CONCURRENCY $PORT blocking $RUN
        benchmark $CONCURRENCY $PORT async $RUN
      else
        benchmark $CONCURRENCY $PORT blocking $RUN
      fi
    done
  done
done
