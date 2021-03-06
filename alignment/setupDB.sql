CREATE TABLE IF NOT EXISTS `alignment_collection` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `secret` varchar(100) NOT NULL,
  `taxon_group` varchar(100) NOT NULL,
  `glossary_path1` varchar(300),
  `glossary_path2` varchar(300),
  `ontology_path` varchar(300),
  `lastretrieved` TIMESTAMP NOT NULL DEFAULT 0,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
