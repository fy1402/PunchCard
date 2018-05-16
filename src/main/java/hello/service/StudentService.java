package hello.service;

import hello.model.Student;

import java.util.Map;

/**
 * Created by i-feng on 2017/9/6.
 */
public interface StudentService {

    public abstract void add(Student user);

    public abstract void delete(String id);

    public abstract void update(Student user);

    public abstract Student find(String id);

    public abstract Map<String, Student> getAll();
}
