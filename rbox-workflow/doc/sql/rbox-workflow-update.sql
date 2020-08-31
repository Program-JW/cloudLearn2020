INSERT INTO api_log (
	object_id,
	request_data,
	response_data,
	create_on,
	`status`
) SELECT
	apply_id,
	request_data,
	response_data,
	create_on,
	`status`
FROM
	stock_change_log;