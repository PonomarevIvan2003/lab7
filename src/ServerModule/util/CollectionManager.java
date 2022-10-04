package ServerModule.util;


import common.data.Flat;
import common.data.Transport;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * Класс, описывающий коллекцию и определяющий взаимодействие с ней
 */
public class CollectionManager {
    private TreeMap<Integer, Flat> myCollection = new TreeMap();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private ReadWriteLock locker = new ReentrantReadWriteLock();
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager){
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection();
    }

    /**
     * @return The collection itself.
     */
    public TreeMap<Integer, Flat> getCollection() {
        return myCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return myCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return myCollection.size();
    }

    /**
     * Generates next ID. It will be (the bigger one + 1).
     * @return Next ID.
     */
    public int generateId() {
        if (myCollection.isEmpty()) return 1;
        int lastId = 0;
        for (Flat flat : myCollection.values()) {
            lastId = Math.max(lastId, flat.getId());
        }
        return lastId + 1;
    }

    /**
     * Remove flats greater than the selected one.
     * @param flat A Flat to compare with.
     */
    public Map<Integer, Flat> getGreater(Flat flat) {
        locker.readLock().lock();
        Map<Integer, Flat> mapToDelete;
        try {
            mapToDelete = new HashMap<>();
            for (Map.Entry<Integer, Flat> entry : myCollection.entrySet()) {
                if (entry.getValue().compareTo(flat) > 0) {
                    mapToDelete.put(entry.getKey(), entry.getValue());
                }
            }
        } finally {
            locker.readLock().unlock();
        }
        return mapToDelete;
    }

    /**
     * Remove flats greater than the selected one.
     * @param flat A Flat to compare with.
     */
    public Map<Integer, Flat> getLower(Flat flat) {
        locker.readLock().lock();
        Map<Integer, Flat> mapToDelete;
        try {
            mapToDelete = new HashMap<>();
            for (Map.Entry<Integer, Flat> entry : myCollection.entrySet()) {
                if (entry.getValue().compareTo(flat) < 0) {
                    mapToDelete.put(entry.getKey(), entry.getValue());
                }
            }
        } finally {
            locker.readLock().unlock();
        }
        return mapToDelete;
    }

    /**
     * Remove flats greater than the selected one.
     * @param keyToCompare A Key to compare with.
     */
    public Map<Integer, Flat> getGreaterKey(int keyToCompare) {
        locker.readLock().lock();
        Map<Integer, Flat> mapToDelete;
        try {
            mapToDelete = new HashMap<>();
            for(Map.Entry<Integer, Flat> entry : myCollection.entrySet()) {
                if (entry.getKey() > keyToCompare) {
                    mapToDelete.put(entry.getKey(), entry.getValue());
                }
            }
        } finally {
            locker.readLock().unlock();
        }
        return mapToDelete;
    }

    /**
     * @param key The key used to take the marine.
     * @return A marine's key.
     */
    public Flat getFromCollection(int key) {
        locker.readLock().lock();
        Flat flat;
        try {
            flat = myCollection.get(key);
        } finally {
            locker.readLock().unlock();
        }
        return flat;
    }

    /**
     * @param id ID of the flat.
     * @return flat by his ID or null if flat isn't found.
     */
    public Integer getKeyById(int id) {
        locker.readLock().lock();
        Integer key;
        try {
            key = myCollection.entrySet().stream().filter(entry -> entry.getValue().getId() == id).map(Map.Entry::getKey).findFirst().get();
        } finally {
            locker.readLock().unlock();
        }
        return key;
    }

    /**
     * Adds a new flat to collection.
     * @param flat A Flat to add.
     */
    public void addToCollection(int key, Flat flat) {
        locker.writeLock().lock();
        try {
            myCollection.put(key, flat);
        } finally {
            locker.writeLock().unlock();
        }
    }


    public String filterByTransport(Transport transport) {
        locker.readLock().lock();
        StringBuilder info;
        try {
            if (myCollection.isEmpty()) return "Коллекция пуста!\n";
            List<Flat> filteredList = myCollection.values().stream().filter(x -> x.getTransport().equals(transport)).collect(Collectors.toList());

            info = new StringBuilder();
            for (Flat flat : filteredList) {
                info.append(flat.toString());
                info.append("\n\n");
            }
        } finally {
            locker.readLock().unlock();
        }
        return info.toString();
    }


    public String filterLessThanNew(boolean nnew) {
        locker.readLock().lock();
        StringBuilder info;
        try {
            if (myCollection.isEmpty()) return "Коллекция пуста!\n";
            List<Flat> filteredList = myCollection.values().stream().filter(x -> x.getNewness() != nnew).collect(Collectors.toList());

            info = new StringBuilder();
            for (Flat flat : filteredList) {
                info.append(flat.toString());
                info.append("\n\n");
            }
        } finally {
            locker.readLock().unlock();
        }
        return info.toString();
    }

    public String ascendingHeight() {
        locker.readLock().lock();
        StringBuilder info;
        try {
            if (myCollection.isEmpty()) return "Коллекция пуста!\n";

            List<Flat> sortedCollection = myCollection.values().stream().sorted(Comparator.comparing(Flat::getHeight)).collect(Collectors.toList());
            info = new StringBuilder();
            for (Flat flat : sortedCollection) {
                info.append(flat.getHeight());
                info.append("\n\n");
            }
        } finally {
            locker.readLock().unlock();
        }
        return info.toString();
    }

    /**
     * Removes a new flat to collection.
     * @param key A key to remove.
     */
    public void removeFromCollection(int key) {
        locker.writeLock().lock();
        try {
            myCollection.remove(key);
        } finally {
            locker.writeLock().unlock();
        }
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        locker.writeLock().lock();
        try {
            myCollection.clear();
        } finally {
            locker.writeLock().unlock();
        }
    }

    /**
     * Loads the collection from file.
     */
    public void loadCollection() {
        myCollection = databaseCollectionManager.getCollection();
        lastInitTime = LocalDateTime.now();
        System.out.println("Коллекция загружена.");
    }

    @Override
    public String toString() {
        locker.readLock().lock();
        if (myCollection.isEmpty()) return "Коллекция пуста!\n";

        List<Flat> printCollection = myCollection.values().stream().sorted(Comparator.comparing(Flat::getCoordinates)).collect(Collectors.toList());
        StringBuilder info;
        try {
            info = new StringBuilder();
            for (Flat flat : printCollection) {
                info.append(flat);
                info.append("\n\n");
            }
        } finally {
            locker.readLock().unlock();
        }
        return info.toString();
    }
}
