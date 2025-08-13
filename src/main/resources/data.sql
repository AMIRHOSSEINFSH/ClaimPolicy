INSERT INTO vehicle (id, brand, model, year)
VALUES ('fcbdd297-96e1-481d-9a5f-b035f5527a40', 'peju', '405', '1401');

INSERT INTO policy (id, vehicle_id, amount, enabled, expiry_date,doc_is_needed)
VALUES (
           '9de13f89-9b56-42f7-befd-44755f8e0774',
           'fcbdd297-96e1-481d-9a5f-b035f5527a40',
           150000000.00,
           TRUE,
           '2025-12-31 23:59:59',
            true
       );
