ALTER TABLE auto_user
ADD COLUMN auto_post_id int REFERENCES auto_post(id);