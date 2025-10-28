#!/bin/bash


echo Call vendors through okapi


echo Submit

curl --header "X-Okapi-Tenant: diku" -H "Content-Type: application/json" http://localhost:9130/vendor/xxxyz -X PUT -d '
"id": "588b5c42-8634-4af7-bc9b-5e0116ed96b6",
      "name": "GOBI",
      "code": "AQ-GOBI",
      "description": "This is Yankee Book Peddler.",
      "vendor_status": "Active",
      "language": "en-us",
      "aliases": [
        {
          "value": "YBP",
          "description": "AKA"
        },
        {
          "value": "Yankee Book Peddler",
          "description": "Formerly known as"
        }
      ],
      "addresses": [
        {
          "address": {
            "addressLine1": "10 Estes Street",
            "addressLine2": "",
            "city": "Ipswich",
            "stateRegion": "MA",
            "zipCode": "01938",
            "country": "USA"
          },
          "categories": [
            "0e3f9680-ab06-4565-af64-609b7364e6eb",
            "996ecd31-7ca4-4d8d-9bbf-bc94dff5f6c6",
            "112ae2e4-88ae-4fa5-a75b-2379d2035e52",
            "56288fe8-8037-44da-8395-01d2d106dc54"
          ],
          "language": "en",
          "san_code": "1234567"
        }
      ],
      "phone_numbers": [
        {
          "phone_number": {
            "country_code": "US",
            "area_code": "978",
            "phone_number": "9999999"
          },
          "categories": [
            "0e3f9680-ab06-4565-af64-609b7364e6eb",
            "996ecd31-7ca4-4d8d-9bbf-bc94dff5f6c6"
          ],
          "language": "en-us"
        }
      ],
      "emails": [
        {
          "email": {
            "value": "noreply@folio.org",
            "description": "Main"
          },
          "categories": [
            "112ae2e4-88ae-4fa5-a75b-2379d2035e52",
            "56288fe8-8037-44da-8395-01d2d106dc54"
          ],
          "language": "en-us"
        }
      ],
      "urls": [
        {
          "url": {
            "value": "noreply@folio.org",
            "description": ""
          },
          "language": "en-us",
          "categories": [
            "112ae2e4-88ae-4fa5-a75b-2379d2035e52",
            "56288fe8-8037-44da-8395-01d2d106dc54"
          ],
          "notes": ""
        }
      ],
      "contacts": [
        {
          "language": "en-us",
          "contact_person": {
            "prefix": "Director",
            "first_name": "Nick",
            "last_name": "Fury",
            "language": "en-us",
            "notes": "SHIELDs Big Kahuna",
            "phone_number": {
              "country_code": "US",
              "area_code": "978",
              "phone_number": "9999999"
            },
            "email": {
              "value": "noreply@folio.org",
              "description": "Main"
            },
            "address": {
              "addressLine1": "10 Estes Street",
              "addressLine2": "",
              "city": "Ipswich",
              "stateRegion": "MA",
              "zipCode": "01938",
              "country": "USA"
            },
            "url": {
              "value": "noreply@folio.org",
              "description": ""
            }
          },
          "categories": [
            "08e0eb27-b57f-4638-a703-9a2c57bd8708",
            "a5da9c44-6619-403f-bd3b-f9bd2f63bc59",
            "da0272e4-7ff7-4ea8-9bc9-9d9cd5c81580",
            "ab18897b-0e40-4f31-896b-9c9adc979a88"
          ]
        }
      ],
      "agreements": [
        {
          "name": "History Follower Incentive",
          "discount": 10,
          "reference_url": "http://my_sample_agreement.com",
          "notes": ""
        }
      ],
      "erp_code": "AQ-GOBI-HIST",
      "payment_method": "EFT",
      "access_provider": true,
      "governmental": true,
      "licensor": true,
      "material_supplier": true,
      "vendor_currencies": [
        "USD",
        "CAD",
        "GBP"
      ],
      "claiming_interval": 30,
      "discount_percent": 10,
      "expected_activation_interval": 1,
      "expected_invoice_interval": 5,
      "renewal_activation_interval": 1,
      "subscription_interval": 365,
      "liable_for_vat": false,
      "tax_id": "TX-GOBI-HIST",
      "tax_percentage": 5,
      "edi": {
        "vendor_edi_code": "AQ-GOBI-HIST",
        "vendor_edi_type": "014/EAN",
        "lib_edi_code": "MY-LIB-1",
        "lib_edi_type": "014/EAN",
        "prorate_tax": true,
        "prorate_fees": true,
        "edi_naming_convention": "",
        "send_acct_num": true,
        "support_order": true,
        "support_invoice": true,
        "notes": "",
        "edi_ftp": {
          "ftp_format": "SFTP",
          "server_address": "http://127.0.0.1",
          "username": "edi_username",
          "password": "edi_password",
          "ftp_mode": "ASCII",
          "ftp_conn_mode": "Active",
          "ftp_port": "22",
          "order_directory": "/path/to/order/directory",
          "invoice_directory": "/path/to/invoice/directory",
          "notes": "My FTP notes"
        },
        "edi_job": {
          "schedule_edi": false,
          "date": null,
          "time": null,
          "is_monday": false,
          "is_tuesday": false,
          "is_wednesday": false,
          "is_thursday": false,
          "is_friday": false,
          "is_saturday": false,
          "is_sunday": false,
          "send_to_emails": "email1@site.com, email2@site.com",
          "notify_all_edi": true,
          "notify_invoice_only": true,
          "notify_error_only": false
        }
      },
      "interfaces": [
        {
          "name": "Sales Portal",
          "uri": "https://www.gobi3.com/Pages/Login.aspx",
          "username": "my_user",
          "password": "my_password",
          "notes": "This is the store-front for GOBI.",
          "available": true,
          "delivery_method": "Online",
          "statistics_format": "PDF",
          "locally_stored": "",
          "online_location": "",
          "statistics_notes": ""
        }
      ],
      "accounts": [
        {
          "name": "History Account",
          "account_no": "GOBI-HIST-12",
          "description": "This is my account description.",
          "app_system_no": "FIN-GOBI-HIST-12",
          "payment_method": "EFT",
          "account_status": "Active",
          "contact_info": "Some basic contact information note.",
          "library_code": "My Library",
          "library_edi_code": "MY-LIB-1",
          "notes": ""
        }
      ],
      "changelogs": [
        {
          "description": "This is a sample note.",
          "timestamp": "2008-05-15T03:53:00-07:00"
        }
      ]
}
'

echo search

curl --header "X-Okapi-Tenant: diku" http://localhost:9130/vendor -X GET

./okapi-cmd /vendor?query=code%3d%3dSREBSCO

