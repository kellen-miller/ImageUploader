package com.kellen.ReactAws.dao;

import com.kellen.ReactAws.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("SqlResolve")
@Repository("postgres")
public class UserProfileDataAccessService implements UserProfileDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertUserProfile(UUID id, UserProfile userProfile) {

        final String sql = "INSERT INTO user_profile (id, username, image_link) VALUES (?::uuid, ?, ?)";
        jdbcTemplate.update(
                sql,
                new Object[]{id, userProfile.getUsername(), userProfile.getUserProfileImageLink().orElse(null)},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
        );
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        final String sql = "UPDATE user_profile SET username = ?, image_link = ? WHERE id = (?::uuid)";
        jdbcTemplate.update(
                sql,
                new Object[]{
                        userProfile.getUsername(),
                        userProfile.getUserProfileImageLink().orElse(null),
                        userProfile.getUserProfileId()
                },
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
        );
    }

    @Override
    public Optional<UserProfile> getUserProfileById(UUID id) {
        final String sql = "SELECT id, username, image_link FROM user_profile WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (resultSet, i) -> new UserProfile(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("username"),
                        resultSet.getString("image_link")
                ))
        );
    }

    @Override
    public List<UserProfile> getAllUserProfiles() {
        final String sql = "SELECT id, username, image_link FROM user_profile";
        return jdbcTemplate.query(sql, (resultSet, i) -> new UserProfile(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("username"),
                resultSet.getString("image_link")
        ));
    }
}
