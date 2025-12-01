-- Update existing orders to have shippingFee = 0 if null
UPDATE `order` SET shippingFee = 0 WHERE shippingFee IS NULL;
