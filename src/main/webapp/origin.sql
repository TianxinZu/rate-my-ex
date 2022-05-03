USE ratemyex;

INSERT INTO Username (email, username, userpassword) VALUE ("test@test.test", "test1", "test1");
INSERT INTO Person (name, gender, overall_rating, rating_count) VALUE ("Wa", "Female", 3.5, 1);
INSERT INTO Post (personid, userid, description, rating) VALUE (1, 1, "Great", 3.5);