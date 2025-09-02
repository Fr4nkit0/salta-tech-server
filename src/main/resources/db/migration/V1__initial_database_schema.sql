-- -------------------------------------------------------
-- V1__initial_database_schema.sql
-- Migración inicial: crea todas las tablas de la base de datos de Salta Tech
-- -------------------------------------------------------
-- 1) Tabla 'users'
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255)          NOT NULL UNIQUE,
    password VARCHAR(255)       NOT NULL,
    phone_number VARCHAR(255)   NULL,
    is_super_user BOOLEAN       NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL
);
-- 2) Tabla 'organizations'
CREATE TABLE IF NOT EXISTS organizations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(50) NOT NULL UNIQUE,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL
);
-- 3) Tabla 'branches'
CREATE TABLE IF NOT EXISTS branches (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    organization_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT  NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- -------------------------------------------------------
-- Tablas de seguridad: control de acceso basado en roles
-- -------------------------------------------------------
-- 4) Tabla 'modules'
CREATE TABLE IF NOT EXISTS modules (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    base_path VARCHAR(255) NOT NULL
);
-- 5) Tabla 'operations'
CREATE TABLE IF NOT EXISTS operations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    http_method VARCHAR(10) NOT NULL,
    permit_all BOOLEAN NOT NULL,
    module_id BIGINT,
    FOREIGN KEY (module_id) REFERENCES modules(id)
);
-- 6) Tabla 'roles'
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    organization_id BIGINT NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations(id)
);
-- 7) Tabla 'granted_permissions'
CREATE TABLE IF NOT EXISTS granted_permissions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_id BIGINT NOT NULL,
    operation_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (operation_id) REFERENCES operations(id)
);
-- 8) Tabla 'organizations_members'
CREATE TABLE IF NOT EXISTS organizations_members (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
-- 9) Tabla 'refresh_tokens'
CREATE TABLE IF NOT EXISTS refresh_tokens(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token VARCHAR(2048) NOT NULL,
    expiration TIMESTAMP NOT NULL,
    is_valid BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- 10) Tabla 'branches_access'
CREATE TABLE IF NOT EXISTS branches_access(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    organization_member_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    FOREIGN KEY (organization_member_id) REFERENCES organizations_members(id),
    FOREIGN KEY (branch_id) REFERENCES branches(id)
);
-- -------------------------------------------------------
-- Fin de las tablas de seguridad
-- -------------------------------------------------------
-- 11) Tabla 'people'
CREATE TABLE IF NOT EXISTS people (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    phone_number VARCHAR(255),
    dni VARCHAR(255) NULL UNIQUE,
    cuil VARCHAR(255) NULL UNIQUE,
    gender VARCHAR(20),
    birth_date DATE,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
 );

-- 12) Tabla 'customers'
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status VARCHAR(100) NULL,
    person_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES people(id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- 13) Tabla categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id)
);
-- 14) Tabla brands
CREATE TABLE IF NOT EXISTS brands (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id)
);
-- 14) Tabla products
CREATE TABLE IF NOT EXISTS products (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    available_quantity INTEGER NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (brand_id) REFERENCES brands(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- 15) Tabla branch_stocks
CREATE TABLE IF NOT EXISTS branch_stocks (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (branch_id) REFERENCES branches(id)
);
-- 16) Tabla sales
CREATE TABLE IF NOT EXISTS sales (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    customer_id BIGINT NULL,
    status VARCHAR(40) NOT NULL,
    total NUMERIC(15, 2) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    FOREIGN KEY (branch_id) references branches(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- 19) Tabla sales details
CREATE TABLE IF NOT EXISTS sales_details (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    organization_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- 20) Tabla Trasactions
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    type VARCHAR(3) NOT NULL,
    amount NUMERIC(15, 2) NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- 21) Tabla payment_methods
CREATE TABLE IF NOT EXISTS payment_methods (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(30),
    type VARCHAR(30) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);
-- 22) Tabla payments
CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    organization_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    sale_id BIGINT ,
    payment_method_id BIGINT NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    description VARCHAR(255) NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NULL,
    deleted_date TIMESTAMP NULL,
    enabled BOOLEAN NOT NULL,
    created_by BIGINT NOT NULL,
    last_modified_by BIGINT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations (id),
    FOREIGN KEY (branch_id) REFERENCES branches (id),
    FOREIGN KEY (transaction_id) REFERENCES transactions (id),
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (last_modified_by) REFERENCES users(id)
);