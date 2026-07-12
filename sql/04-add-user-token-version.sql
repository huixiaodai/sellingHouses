ALTER TABLE `user`
    ADD COLUMN token_version INT NOT NULL DEFAULT 0 COMMENT 'Token版本号';
