CREATE TABLE `t_leave`
(
    `id`                  bigint(20) NOT NULL,
    `process_instance_id` varchar(64)  DEFAULT NULL,
    `start_time`          datetime     DEFAULT NULL,
    `end_time`            datetime     DEFAULT NULL,
    `reason`              varchar(128) DEFAULT NULL,
    `leader_approved`     varchar(4)   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;