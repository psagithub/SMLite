delimiter $$

CREATE DATABASE `vendor` /*!40100 DEFAULT CHARACTER SET latin1 */$$

CREATE TABLE `vendor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `purchaseOrderAvailable` int(1) DEFAULT NULL,
  `purchaseNumber` varchar(50) DEFAULT NULL,
  `orderType` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `purchaseType_idx` (`orderType`),
  CONSTRAINT `id` FOREIGN KEY (`orderType`) REFERENCES `order_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1$$

CREATE TABLE `order_type` (
  `id` int(11) NOT NULL,
  `name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

INSERT INTO `vendor`.`order_type`(`id`,`name`) VALUES(1,'Staff Augmentation')$$
INSERT INTO `vendor`.`order_type`(`id`,`name`) VALUES(2,'Outbound Projects')$$
INSERT INTO `vendor`.`order_type`(`id`,`name`) VALUES(3,'Software Licensing')$$
INSERT INTO `vendor`.`order_type`(`id`,`name`) VALUES(4,'Hardware Purchase')$$


