-- V2__seed_initial_data.sql
-- Inserta datos iniciales de usuarios, roles, organizaciones y branches
DO
$$
DECLARE
  v_super_user_id BIGINT;
  v_org_id BIGINT;
  v_module_auth BIGINT;
  v_module_customers BIGINT;
  v_module_sales BIGINT;
  v_module_payments BIGINT;
  v_module_payment_methods BIGINT;
  v_module_products BIGINT;
  v_module_brands BIGINT;
  v_module_categories BIGINT;
  v_role_id BIGINT;
  v_user_id BIGINT;
  v_org_member_id BIGINT;
  v_branch1_id BIGINT;
  v_branch2_id BIGINT;
BEGIN
-- SUPER USER (si no existe)
IF NOT EXISTS (SELECT 1 FROM users WHERE email='saltatech@gmail.com') THEN
INSERT INTO users (
  first_name, last_name, email, password, phone_number, is_super_user, created_date, enabled
)
VALUES (
  'Franco', 'Calisaya', 'saltatech@gmail.com',
  '$2a$10$n8RSNlaV8qX2pyYczwWRn.mwNMXBoONyyJrv9cKvWBxJ/Yf24X8Ki',
  '+543874103402', TRUE, NOW(), TRUE
)
RETURNING id INTO v_super_user_id;
END IF;

-- ORGANIZACIÓN (si no existe)
IF NOT EXISTS (SELECT 1 FROM organizations WHERE slug='market-la-familia') THEN
INSERT INTO organizations (name, slug, created_date, enabled)
VALUES ('LA FAMILIA ','market-la-familia',NOW(), TRUE)
RETURNING id INTO v_org_id;
ELSE
SELECT id INTO v_org_id FROM organizations WHERE slug='market-la-familia';
END IF;

-- MÓDULOS
INSERT INTO modules (name, base_path)
SELECT * FROM (VALUES
    ('AUTH', '/auth'),
    ('CUSTOMER', '/customers'),
    ('SALES', '/sales'),
    ('PAYMENTS', '/payments'),
    ('PAYMENT_METHODS', '/payment-methods'),
    ('PRODUCTS', '/products'),
    ('BRANDS', '/brands'),
    ('CATEGORIES', '/categories')
) AS t(name, base_path)
WHERE NOT EXISTS (SELECT 1 FROM modules WHERE name=t.name);

-- OBTENER LOS IDs DE CADA MÓDULO
SELECT id INTO v_module_auth FROM modules WHERE name='AUTH';
SELECT id INTO v_module_customers FROM modules WHERE name='CUSTOMER';
SELECT id INTO v_module_sales FROM modules WHERE name='SALES';
SELECT id INTO v_module_payments FROM modules WHERE name='PAYMENTS';
SELECT id INTO v_module_payment_methods FROM modules WHERE name='PAYMENT_METHODS';
SELECT id INTO v_module_products FROM modules WHERE name='PRODUCTS';
SELECT id INTO v_module_brands FROM modules WHERE name='BRANDS';
SELECT id INTO v_module_categories FROM modules WHERE name='CATEGORIES';

-- OPERACIONES
INSERT INTO operations (name, path, http_method, permit_all, module_id)
SELECT * FROM (VALUES
('AUTHENTICATE', '/authenticate', 'POST', true, v_module_auth),
('LOGOUT', '/logout', 'POST', true, v_module_auth),
('REFRESH','/refresh','POST',true,v_module_auth),
('ME','/me','GET',true,v_module_auth),
('PROFILE','/profile','GET',true,v_module_auth),
('READ_ONE_CUSTOMER', '/[0-9]+', 'GET', false, v_module_customers),
('READ_ALL_CUSTOMERS', '', 'GET', false, v_module_customers),
('CREATE_ONE_CUSTOMER', '', 'POST', false, v_module_customers),
('UPDATE_ONE_CUSTOMER', '/[0-9]+', 'POST', false, v_module_customers),
('DELETE_ONE_CUSTOMER', '/[0-9]+', 'DELETE', false, v_module_customers)
) AS t(name, path, http_method, permit_all, module_id)
WHERE NOT EXISTS (SELECT 1 FROM operations WHERE name=t.name);

-- ROL
IF NOT EXISTS (SELECT 1 FROM roles WHERE name='ADMINISTRATOR' AND organization_id=v_org_id) THEN
INSERT INTO roles (name, organization_id)
VALUES ('ADMINISTRATOR', v_org_id)
RETURNING id INTO v_role_id;
ELSE
SELECT id INTO v_role_id FROM roles WHERE name='ADMINISTRATOR' AND organization_id=v_org_id;
END IF;

-- ASIGNAR PERMISOS AL ROL
INSERT INTO granted_permissions (role_id, operation_id)
SELECT v_role_id, id FROM operations
WHERE NOT EXISTS (
SELECT 1 FROM granted_permissions gp
WHERE gp.role_id=v_role_id AND gp.operation_id=operations.id
);

-- USUARIO NORMAL
IF NOT EXISTS (SELECT 1 FROM users WHERE email='reyes.1992@gmail.com') THEN
INSERT INTO users (first_name, last_name, email, password, phone_number, is_super_user, created_date, enabled)
VALUES ('Norma', 'Reyes', 'reyes.1992@gmail.com',
    '$2a$10$n8RSNlaV8qX2pyYczwWRn.mwNMXBoONyyJrv9cKvWBxJ/Yf24X8Ki',
    '+543873896102', FALSE, NOW(), TRUE)
RETURNING id INTO v_user_id;
ELSE
SELECT id INTO v_user_id FROM users WHERE email='reyes.1992@gmail.com';
END IF;

-- ORGANIZATION MEMBER
IF NOT EXISTS (
SELECT 1 FROM organizations_members
WHERE user_id=v_user_id AND organization_id=v_org_id AND role_id=v_role_id
) THEN
INSERT INTO organizations_members (user_id, organization_id, role_id)
VALUES (v_user_id, v_org_id, v_role_id)
RETURNING id INTO v_org_member_id;
ELSE
SELECT id INTO v_org_member_id
FROM organizations_members
WHERE user_id=v_user_id AND organization_id=v_org_id AND role_id=v_role_id;
END IF;

-- BRANCHES
IF NOT EXISTS (SELECT 1 FROM branches WHERE name='Local Huaico' AND organization_id=v_org_id) THEN
INSERT INTO branches (name, organization_id, created_date, enabled, created_by)
VALUES ('Local Huaico', v_org_id, NOW(), TRUE, v_org_member_id)
RETURNING id INTO v_branch1_id;
END IF;

IF NOT EXISTS (SELECT 1 FROM branches WHERE name='Local Castañares' AND organization_id=v_org_id) THEN
INSERT INTO branches (name, organization_id, created_date, enabled, created_by)
VALUES ('Local Castañares', v_org_id, NOW(), TRUE, v_org_member_id)
RETURNING id INTO v_branch2_id;
END IF;

-- BRANCH ACCESS
INSERT INTO branches_access (organization_member_id, branch_id)
SELECT v_org_member_id, id FROM branches b
WHERE b.organization_id=v_org_id
AND NOT EXISTS (
SELECT 1 FROM branches_access ba
WHERE ba.organization_member_id=v_org_member_id AND ba.branch_id=b.id
);

END
$$;
