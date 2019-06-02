import java.util.Iterator;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * An implementation of a binary heap. The heap is ordered with the minimum
 * as the first element. How the minimum is calculated depends on the 
 * Comparator supplied in the constructor. A map is kept for fast lookup of
 * the indices of each element in the heap.
 * 
 * @param <E> the type of objects in the heap.
 */

public class BinHeapWithIndexTracker<E> implements PrioQueue<E> {
	
	
	/** The default capacity of the ArrayList representing the binary heap. */
	private static final int DEFAULT_CAPACITY = 10;
	
	/** The ArrayList that represents the binary heap. */
	private ArrayList<E> array;
	
	/** The map that keeps track of the indices of each element in the array. */
	private Map<E, List<Integer>> map;
	
	/** The current amount of elements in the binary heap. */
	private int currentSize;
	
	/** The Comparator used to sort element in the binary heap.*/
	private Comparator<? super E> comp;
	
	/**
	 * Create a empty BinHeap with size 10 and a specified Comparator 
	 * which is used to sort the binary heap.
	 * 
	 * @param comp the Comparator that will be used to sort the BinHeap.
	 */
	public BinHeapWithIndexTracker(Comparator<? super E> comp) {
		this.comp = comp;
		array = new ArrayList<>(DEFAULT_CAPACITY);
		currentSize = 0;
		map = new HashMap<>();
	}


	/**
	 * Implements add(E element) of PrioQueue. Adds e to the heap. Keeps the heap 
	 * ordered. Adds and updates the indices of element and any moved elements.
	 * 
	 * @param element the item to be added.
	 */
	@Override
	public void add(E element) {
		if (currentSize++ == 0) {
			array.add(0,element);
			array.add(1,element);
			updateIndex(element, 1, -1);
		} else {
			int hole = currentSize;
			updateIndex(element, hole, -1);
			percolateUp(hole, element);
		}
	}

	/**
	 * Implements peek() of PrioQueue<E>. Returns the minimum object in the
	 * binary heap.
	 * 
	 * @return the minimum object of the heap or null if the heap is empty.
	 */
	@Override
	public E peek() {
		if (currentSize == 0) {
			return null;
		}
		return array.get(1);
	}

	/**
	 * Implements poll() of PrioQueue<E>. Returns and removes the minimum 
	 * element in the binary heap. Updates the indices of the minimum element 
	 * and of any moved elements.
	 * 
	 * @return the minimum object of the heap or null if the heap is empty.
	 */
	@Override
	public E poll() {
		if (currentSize == 0) {
			return null;
		}
		E minItem = peek();
		updateIndex(minItem, -1, 1);
		// Set last element first and update its index. Decrease currentSize
		// with one.
		array.set(1, array.get(currentSize));
		updateIndex(array.get(1), 1, currentSize--);
		
		percolateDown(1);
		return minItem;
	}

	/**
	 * Implements add(E e) of PrioQueue<E>. Removes the first occurence of the
	 * specified object e. Does nothing if the object is not in the heap.
	 * Updates the index of the removed element and any elements that have been
	 * moved.
	 * 
	 * @param e the item to be removed.
	 */
	@Override
	public void remove(E element) {
		// If element not found, i.e. index = null, return;
		if (map.get(element) == null) {
			return;
		}
		// Get indices of element and remove the first of them.
		List<Integer> indicesOfRemovedElement = map.get(element);
		int removeIndex = indicesOfRemovedElement.get(0);
		updateIndex(element, -1, removeIndex);
		
		// Set last element at the index of the removed element.
		// Decrease currentSize by one.
		array.set(removeIndex, array.get(currentSize));
		updateIndex(array.get(removeIndex), removeIndex, currentSize--);
		// Make sure bin heap is heap ordered by doing percolateUp and 
		// percolateDown.
		percolateUp(removeIndex, array.get(removeIndex));
		percolateDown(removeIndex);
	}
	
	/**
	 * Performs several percolate/bubble up's starting from index hole with 
	 * element element until e is in its correct place. Update indices of any
	 * moved elements.
	 * 
	 * @param hole the position where percolateUp starts.
	 * @param element the object that should start at index hole and be compared
	 * to its parents.
	 */
	public void percolateUp(int hole, E element) {
		int startIndexOfElement = hole;
		
		for (array.set(0, element); comp.compare(element, array.get(hole/2)) < 0; hole /= 2) {
			if (hole == currentSize) {
				array.add(hole, array.get(hole/2));
			}
			else {
				array.set(hole, array.get(hole/2));
			}
			updateIndex(array.get(hole), hole, hole/2);
		}
		if (hole == currentSize) {
			array.add(hole, element);
		}
		else {
			array.set(hole, element);
		}
		updateIndex(element, hole, startIndexOfElement);
	}
	
	/**
	 * Performs several percolate/bubble down's starting from index hole with
	 * the element at this index until that element is in its correct place.
	 * Updates the indices of any moved elements.
	 * 
	 * @param hole the position where percolateDown starts.
	 */
	private void percolateDown(int hole) {
		
		int child;
		E tmp = array.get(hole);
		int startIndexOfTmp = hole;
		
		for (; hole * 2 <= currentSize; hole = child) {
			child = hole * 2;
			if (child != currentSize && comp.compare(array.get(child+1), array.get(child)) < 0) {
				child++;
			}
			if (comp.compare(array.get(child), tmp) < 0) {
				array.set(hole, array.get(child));
				updateIndex(array.get(child), hole, child);
			} else {
				break;
			}
		}
		array.set(hole, tmp);
		updateIndex(tmp, hole, startIndexOfTmp);
	}
	
	/**
	 * Implements iterator() of PrioQueue<E>. Creates a new iterator used to
	 * iterate through the binary heap.
	 * 
	 * @return an iterator over the objects in the heap.
	 */
	@Override
	public Iterator<E> iterator() {
		return new BinHeapIterator();
	}
	
	/**
	 * Overrides toString() of Object.
	 * Returns a String of the ArrayList representing the binary heap.
	 * 
	 * return String representation of the heap.
	 */
	@Override
	public String toString() {
		String str = "Array: [";
		for (int i = 1; i < currentSize; i++) {
			str += array.get(i) + " ";
		}
		str += array.get(currentSize) + "]";
		str += "\n Map: " + map;
		return str;
	}
	
	/**
	 * Changes one index of element to a new index in the map. If there are no
	 * indices left for the element, the element is removed from map. If an
	 * index is negative, it won't be added/removed.
	 * 
	 * @param element the element to update indices for.
	 * @param newIndex the new index that will replace the old.
	 * @param oldIndex the old index that will be replaced.
	 */
	private void updateIndex(E element, int newIndex, int oldIndex) {
		// If the indices of element is null, put an empty list of indices
		// there.
		List<Integer> indices = new ArrayList<>();
		if (map.get(element) == null) {
			map.put(element, indices);
		}
		indices = map.get(element);
		if (newIndex != -1) {
			indices.add(new Integer(newIndex));
		}
		if (oldIndex != -1) {
			indices.remove(new Integer(oldIndex));
		}
		if (indices.isEmpty()) {
			map.remove(element);
		}
	}
	
	/**
	 * Inner class BinHeapIterator used to iterate through the array.
	 */
	private class BinHeapIterator implements Iterator<E> {
		
		/** The starting index of the iterator. Index 0 is only used to store
		 * temporary values in the heap. */
		private int current = 1;

		/**
		 * Implements hasNext() of Iterator<E>.
		 * 
		 * @return if there is a next element from where the iterator is
		 * standing.
		 */
		@Override
		public boolean hasNext() {
			return current <= currentSize;
		}

		/**
		 * Implements next() of Iterator<E>.
		 * 
		 * @return the next element in the heap from where the iterator is
		 * standing.
		 * @throws NoSuchElementException if there is no next element.
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			return array.get(current++);
		}
			
		/**
		 * Overrides but does not implement remove() of Iterator<E>.
		 * If the method is used it will throw an UnsuppertedOperationException.
		 * 
		 * @throws UnsuppertedOperationException if the method is called.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}
