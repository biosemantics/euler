CREATE TABLE IF NOT EXISTS `alignment_collection` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `secret` varchar(100) NOT NULL,
  `taxon_group` varchar(100) NOT NULL,
  `glossary_path` varchar(300) NOT NULL,
  `ontology_path` varchar(300) NOT NULL,
  `lastretrieved` TIMESTAMP NOT NULL DEFAULT 0,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;