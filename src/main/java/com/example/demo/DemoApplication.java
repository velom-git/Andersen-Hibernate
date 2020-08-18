package com.example.demo;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;


public class DemoApplication {
    static Configuration configuration = new Configuration().configure();
    static SessionFactory sessionFactory = configuration.buildSessionFactory();
    static Session session = sessionFactory.openSession();

    public static void main(String[] args) {
        create("KingKong", "Stiven King", "Лев Толстой");
        //delete(1L);
        //changeName("Виктор Гюго",1L);
        sessionFactory.close();
    }

    static void changeName(String name, long id){
        try {
            session.getTransaction().begin();

            Author author = session.get(Author.class, id);
            author.setName(name);
            session.saveOrUpdate(author);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    static void create(String bookTitle, String...authorname) {
        try {
            session.getTransaction().begin();

            Book book = new Book();
            book.setTitle(bookTitle);
            String[] auth = authorname;
            for (int i = 0; i < auth.length; i++) {
                Author author = checkExist(auth[i]);
                book.addAuthor(author);
                session.saveOrUpdate(author);
            }

            session.saveOrUpdate(book);

            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    static void delete(long id) {
        try {
            session.getTransaction().begin();

            Author author = session.load(Author.class, id);

            session.delete(author);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    static Author checkExist(String name) {
        Author author = new Author();
        author.setName(name);
        Query query = session.createNativeQuery("select * from Author", Author.class);
        List<Author> allEntity = query.getResultList();
        if (!allEntity.isEmpty()) {
            for (Author x : allEntity) {
                if (name.equals(x.getName())) {
                    author = x;
                }
            }
        }
        return author;
    }

}