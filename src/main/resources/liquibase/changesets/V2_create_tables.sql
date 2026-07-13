CREATE TABLE IF NOT EXISTS users
(
    id         uuid         NOT NULL,
    username   varchar(255) NOT NULL UNIQUE,
    email      varchar(255) NOT NULL UNIQUE,
    password   varchar(255) NOT NULL,
    created_at timestamp    NOT NULL,
    updated_at timestamp,
    deleted_at timestamp,

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tasks
(
    id          uuid         NOT NULL,
    title       varchar(255) NOT NULL,
    description varchar(255),
    status      varchar(50)  NOT NULL,
    created_at  timestamp    NOT NULL,
    updated_at  timestamp,
    deleted_at  timestamp,
    user_id     uuid,

    CONSTRAINT pk_tasks PRIMARY KEY (id),
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id uuid         NOT NULL,
    role    varchar(255) NOT NULL,
    primary key (user_id, role),

    CONSTRAINT fk_users_roles_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);
