-- Fix category name from "Esspersso" to "Espresso"
-- Chạy script này để sửa tên category trong database hiện tại

USE `CoffeShop`;

-- Update category name từ "Esspersso" thành "Espresso"
UPDATE `categories` 
SET `name` = 'Espresso' 
WHERE `name` = 'Esspersso' OR `name` LIKE '%Esspersso%';

-- Kiểm tra kết quả
SELECT * FROM `categories` WHERE `id` = 0 OR `name` LIKE '%Espresso%';

