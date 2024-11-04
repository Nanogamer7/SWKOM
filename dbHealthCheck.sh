#!/bin/bash
if /opt/mssql-tools/bin/sqlcmd -S localhost -U paperless -P paperless -Q 'SELECT 1'; then
    exit 0  # Exit with code 0 for success
else
    exit 1  # Exit with a non-zero code for failure
fi