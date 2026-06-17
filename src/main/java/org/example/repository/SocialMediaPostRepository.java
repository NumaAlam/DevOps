package org.example.repository;

import org.example.HibernateUtil;
import org.example.entity.SocialMediaPost;
import org.hibernate.Session;

import java.util.List;

public class SocialMediaPostRepository {

    public List<SocialMediaPost> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM SocialMediaPost", SocialMediaPost.class).list();
        }
    }
}
