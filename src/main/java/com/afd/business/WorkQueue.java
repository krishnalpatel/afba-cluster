package com.afd.business;

import java.util.LinkedList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "workQueue")
@Scope(value="singleton")
public class WorkQueue {

	//
	// since we are providing the concurrency control, can use non-thread-safe
	// linked list
	//
	private LinkedList<String> workList;
	private boolean done; // no more directories to be added
	private int size; // number of directories in the queue

	public WorkQueue() {
		System.out.println("in WorkQueue Constructor");
		workList = new LinkedList<String>();
		done = false;
		size = 0;
	}

	public synchronized void add(String s) {
	//	System.out.println("in workQueue: add()"+s);
		workList.add(s);
		size++;
		notifyAll();
	}

	public synchronized String remove() {
		//System.out.println("in workQueue: remove()");
		String s;
		while (!done && size == 0) {
			try {
				wait();
			} catch (Exception e) {
			}
			;
		}
		if (size > 0) {
			s = workList.remove();
			size--;
			notifyAll();
		} else
			s = null;
		return s;
	}

	public synchronized void finish() {
	//	System.out.println("in workQueue: finish()");
		done = true;
		notifyAll();
	}

	@Override
	public String toString() {
		return "WorkQueue [workQ=" + workList + ", done=" + done + ", size=" + size + "]";
	}
	
}
