package com.bluewhaledt.saylove.base.util;


import com.bluewhaledt.saylove.network.entities.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ZAArray<E extends BaseEntity> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    @Override
    public synchronized boolean add(E object) {
        if (!contains(object)) {
            return super.add(object);
        }
        return false;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> collection) {
        if (collection != null) {
            HashSet<E> set = new HashSet<E>(this);
            for (E e : collection) {
                if (set.add(e)) {
                    super.add(e);
                }
            }
        }
        return true;
    }

    @Override
    public synchronized void add(int index, E object) {
        if (!contains(object)) {
            super.add(index, object);
        }
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends E> collection) {
        ArrayList<E> list = new ArrayList<E>();
        HashSet<E> set = new HashSet<E>(this);
        for (E e : collection) {
            if (set.add(e)) {
                list.add(e);
            }
        }
        return super.addAll(index, list);
    }
}
