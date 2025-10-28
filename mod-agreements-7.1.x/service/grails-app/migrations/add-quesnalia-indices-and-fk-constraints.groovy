databaseChangeLog = {
	changeSet(author: "Jack_Golding (manual)", id: "20231127-001") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'subscription_agreement', columnNames: 'sa_name')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX subscription_agreement_name_idx ON ${database.defaultSchemaName}.subscription_agreement USING gin (sa_name);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-002") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'subscription_agreement', columnNames: 'sa_description')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX subscription_agreement_description_idx ON ${database.defaultSchemaName}.subscription_agreement USING gin (sa_description);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-003") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'erm_resource', columnNames: 'res_name')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX erm_resource_res_name_idx ON ${database.defaultSchemaName}.erm_resource USING gin (res_name);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-004") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'entitlement', columnNames: 'ent_reference')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX entitlement_reference_idx ON ${database.defaultSchemaName}.entitlement USING gin (ent_reference);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-005") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'entitlement', columnNames: 'ent_description')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX entitlement_description_idx ON ${database.defaultSchemaName}.entitlement USING gin (ent_description);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-006") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'entitlement', columnNames: 'ent_note')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX entitlement_note_idx ON ${database.defaultSchemaName}.entitlement USING gin (ent_note);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-007") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'platform', columnNames: 'pt_name')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX platform_name_idx ON ${database.defaultSchemaName}.platform USING gin (pt_name);".toString()
				sql.execute(cmd);
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-008") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'agreement_relationship', columnNames: 'ar_inward_fk')
			}
		}
		createIndex(indexName: "agreement_relationship_inward_fk_idx", tableName: "agreement_relationship") {
			column(name: "ar_inward_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-009") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'agreement_relationship', columnNames: 'ar_outward_fk')
			}
		}
		createIndex(indexName: "agreement_relationship_outward_fk_idx", tableName: "agreement_relationship") {
			column(name: "ar_outward_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-010") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'alternate_name', columnNames: 'an_owner_fk')
			}
		}
		createIndex(indexName: "alternate_name_owner_fk_idx", tableName: "alternate_name") {
			column(name: "an_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-011") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'comparison_point', columnNames: 'cp_title_list_fk')
			}
		}
		createIndex(indexName: "comparison_point_title_list_fk_idx", tableName: "comparison_point") {
			column(name: "cp_title_list_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-012") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'entitlement', columnNames: 'ent_owner_fk')
			}
		}
		createIndex(indexName: "entitlement_owner_fk_idx", tableName: "entitlement") {
			column(name: "ent_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-013") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'entitlement', columnNames: 'ent_resource_fk')
			}
		}
		createIndex(indexName: "entitlement_resource_fk_idx", tableName: "entitlement") {
			column(name: "ent_resource_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-014") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'holdings_coverage', columnNames: 'co_ent_fk')
			}
		}
		createIndex(indexName: "holdings_coverage_ent_fk_idx", tableName: "holdings_coverage") {
			column(name: "co_ent_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-015") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'internal_contact', columnNames: 'ic_owner_fk')
			}
		}
		createIndex(indexName: "internal_contact_owner_fk_idx", tableName: "internal_contact") {
			column(name: "ic_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-016") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'internal_contact', columnNames: 'ic_user_fk')
			}
		}
		createIndex(indexName: "internal_contact_user_fk_idx", tableName: "internal_contact") {
			column(name: "ic_user_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-017") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'order_line', columnNames: 'pol_owner_fk')
			}
		}
		createIndex(indexName: "order_line_owner_fk_idx", tableName: "order_line") {
			column(name: "pol_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-018") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'order_line', columnNames: 'pol_orders_fk')
			}
		}
		createIndex(indexName: "order_line_orders_fk_idx", tableName: "order_line") {
			column(name: "pol_orders_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-019") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'subscription_agreement', columnNames: 'sa_vendor_fk')
			}
		}
		createIndex(indexName: "subscription_agreement_vendor_fk_idx", tableName: "subscription_agreement") {
			column(name: "sa_vendor_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-020") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'subscription_agreement', columnNames: 'sa_licence_fk')
			}
		}
		createIndex(indexName: "subscription_agreement_licence_fk_idx", tableName: "subscription_agreement") {
			column(name: "sa_licence_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-023") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'subscription_agreement_org_role', columnNames: 'saor_role_fk')
			}
		}
		createIndex(indexName: "subscription_agreement_org_role_role_fk_idx", tableName: "subscription_agreement_org_role") {
			column(name: "saor_role_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-024") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'subscription_agreement_org_role', columnNames: 'saor_owner_fk')
			}
		}
		createIndex(indexName: "subscription_agreement_org_role_owner_fk_idx", tableName: "subscription_agreement_org_role") {
			column(name: "saor_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-025") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'usage_data_provider', columnNames: 'udp_owner_fk')
			}
		}
		createIndex(indexName: "usage_data_provider_owner_fk_idx", tableName: "usage_data_provider") {
			column(name: "udp_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-026") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'document_attachment', columnNames: 'da_type_rdv_fk')
			}
		}
		createIndex(indexName: "document_attachment_type_rdv_fk_idx", tableName: "document_attachment") {
			column(name: "da_type_rdv_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-027") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'templated_url', columnNames: 'tu_resource_fk')
			}
		}
		createIndex(indexName: "templated_url_resource_fk_idx", tableName: "templated_url") {
			column(name: "tu_resource_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-028") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'persistent_job', columnNames: 'job_status_fk')
			}
		}
		createIndex(indexName: "persistent_job_status_fk_idx", tableName: "persistent_job") {
			column(name: "job_status_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-029") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'persistent_job', columnNames: 'job_result_fk')
			}
		}
		createIndex(indexName: "persistent_job_result_fk_idx", tableName: "persistent_job") {
			column(name: "job_result_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-030") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'push_kb_chunk', columnNames: 'pkbc_session_fk')
			}
		}
		createIndex(indexName: "push_kb_chunk_session_fk_idx", tableName: "push_kb_chunk") {
			column(name: "pkbc_session_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-031") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'availability_constraint', columnNames: 'avc_owner_fk')
			}
		}
		createIndex(indexName: "availability_constraint_owner_fk_idx", tableName: "availability_constraint") {
			column(name: "avc_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-032") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'availability_constraint', columnNames: 'avc_body_fk')
			}
		}
		createIndex(indexName: "availability_constraint_body_fk_idx", tableName: "availability_constraint") {
			column(name: "avc_body_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-033") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'content_activation_record', columnNames: 'car_target_kb_fk')
			}
		}
		createIndex(indexName: "content_activation_record_target_kb_fk_idx", tableName: "content_activation_record") {
			column(name: "car_target_kb_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-034") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'content_type', columnNames: 'ct_content_type_fk')
			}
		}
		createIndex(indexName: "content_type_content_type_fk_idx", tableName: "content_type") {
			column(name: "ct_content_type_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-035") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'content_type', columnNames: 'ct_owner_fk')
			}
		}
		createIndex(indexName: "content_type_owner_fk_idx", tableName: "content_type") {
			column(name: "ct_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-036") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'coverage_statement', columnNames: 'cs_resource_fk')
			}
		}
		createIndex(indexName: "coverage_statement_resource_fk_idx", tableName: "coverage_statement") {
			column(name: "cs_resource_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-037") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'embargo', columnNames: 'emb_start_fk')
			}
		}
		createIndex(indexName: "embargo_start_fk_idx", tableName: "embargo") {
			column(name: "emb_start_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-038") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'embargo', columnNames: 'emb_end_fk')
			}
		}
		createIndex(indexName: "embargo_end_fk_idx", tableName: "embargo") {
			column(name: "emb_end_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-039") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'erm_resource', columnNames: 'res_publication_type_fk')
			}
		}
		createIndex(indexName: "res_publication_type_fk_idx", tableName: "erm_resource") {
			column(name: "res_publication_type_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-040") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'identifier_occurrence', columnNames: 'io_status_fk')
			}
		}
		createIndex(indexName: "identifier_occurrence_status_fk_idx", tableName: "identifier_occurrence") {
			column(name: "io_status_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-042") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package_content_item', columnNames: 'pci_pkg_fk')
			}
		}
		createIndex(indexName: "package_content_item_pci_pkg_fk_idx", tableName: "package_content_item") {
			column(name: "pci_pkg_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-043") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package_content_item', columnNames: 'pci_pti_fk')
			}
		}
		createIndex(indexName: "package_content_item_pci_pti_fk_idx", tableName: "package_content_item") {
			column(name: "pci_pti_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-044") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package_content_item', columnNames: 'pci_embargo_fk')
			}
		}
		createIndex(indexName: "package_content_item_pci_embargo_fk_idx", tableName: "package_content_item") {
			column(name: "pci_embargo_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-045") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package_description_url', columnNames: 'pdu_owner_fk')
			}
		}
		createIndex(indexName: "package_description_url_pdu_owner_fk_id", tableName: "package_description_url") {
			column(name: "pdu_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-046") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package', columnNames: 'pkg_vendor_fk')
			}
		}
		createIndex(indexName: "pkg_vendor_fk_idx", tableName: "package") {
			column(name: "pkg_vendor_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-047") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package', columnNames: 'pkg_nominal_platform_fk')
			}
		}
		createIndex(indexName: "pkg_nominal_platform_fk_idx", tableName: "package") {
			column(name: "pkg_nominal_platform_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-048") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'platform_locator', columnNames: 'pl_owner_fk')
			}
		}
		createIndex(indexName: "platform_locator_pl_owner_fk_idx", tableName: "platform_locator") {
			column(name: "pl_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-052") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'platform_title_instance', columnNames: 'pti_ti_fk')
			}
		}
		createIndex(indexName: "platform_title_instance_pti_ti_fk_idx", tableName: "platform_title_instance") {
			column(name: "pti_ti_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231127-053") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'platform_title_instance', columnNames: 'pti_pt_fk')
			}
		}
		createIndex(indexName: "platform_title_instance_pti_pt_fk_idx", tableName: "platform_title_instance") {
			column(name: "pti_pt_fk")
		}
	}

	changeSet(author: "Jack_golding (manual)", id: "20231128-1115-001") {
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.refdata_category SET internal = true
            WHERE rdc_description IN ('StringTemplate.Context')
        """.toString())
      }
    }
  }

	changeSet(author: "Jack_golding (manual)", id: "20231128-1115-002") {
		grailsChange {
			change {
				sql.eachRow("""
					SELECT DISTINCT strt_context
          FROM ${database.defaultSchemaName}.string_template
					WHERE NOT EXISTS (
						SELECT rdv_id FROM ${database.defaultSchemaName}.refdata_value
            WHERE rdv_id = strt_context
					)""".toString()
				) { def row ->
					sql.execute("""
						INSERT INTO ${database.defaultSchemaName}.refdata_value
						(rdv_id, rdv_version, rdv_value, rdv_owner, rdv_label) VALUES
						('${row.strt_context}',
						0,
						'missing_context_${row.strt_context}',
						(
							SELECT rdc_id FROM  ${database.defaultSchemaName}.refdata_category
							WHERE rdc_description='StringTemplate.Context'
						),
						'Missing context ${row.strt_context}'
					)""".toString())
				}
			}
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231128-1115-003") {
    renameColumn(tableName: "string_template", oldColumnName: "strt_context", newColumnName: "strt_context_fk")
  }

	changeSet(author: "Jack_Golding (manual)", id: "20231128-1115-004") {
    addForeignKeyConstraint(baseColumnNames: "strt_context_fk",
        baseTableName: "string_template",
        constraintName: "string_template_context_fk",
        deferrable: "false",
        initiallyDeferred: "false",
        referencedColumnNames: "rdv_id",
        referencedTableName: "refdata_value")
  }

	// POST GOTTINGEN INDICES
	changeSet(author: "Ethan_Freestone (manual)", id: "2024-02-15-001") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'erm_resource', columnNames: 'id,res_sub_type_fk')
			}
		}
		createIndex(indexName: "res_id_sub_type_idx", tableName: "erm_resource") {
			column(name: "id")
			column(name: "res_sub_type_fk")
		}
	}

	changeSet(author: "Ethan_Freestone (manual)", id: "2024-02-15-002") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'erm_resource', columnNames: 'id,res_type_fk')
			}
		}
		createIndex(indexName: "res_id_type_idx", tableName: "erm_resource") {
			column(name: "id")
			column(name: "res_type_fk")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-003") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'identifier_occurrence', columnNames: 'io_id,io_res_fk')
			}
		}
		createIndex(indexName: "io_id_res", tableName: "identifier_occurrence") {
			column(name: "io_id")
			column(name: "io_res_fk")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-004") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'identifier_occurrence', columnNames: 'io_id,io_status_fk')
			}
		}
		createIndex(indexName: "io_id_status", tableName: "identifier_occurrence") {
			column(name: "io_id")
			column(name: "io_status_fk")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-005") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'identifier_occurrence', columnNames: 'io_id,io_res_fk,io_status_fk')
			}
		}
		createIndex(indexName: "io_id_res_status", tableName: "identifier_occurrence") {
			column(name: "io_id")
			column(name: "io_res_fk")
			column(name: "io_status_fk")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-006") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'entitlement', columnNames: 'ent_owner_fk,ent_active_from,ent_active_to')
			}
		}
		createIndex(indexName: "ent_owner_dates_idx", tableName: "entitlement") {
			column(name: "ent_owner_fk")
			column(name: "ent_active_from")
			column(name: "ent_active_to")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-007") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'package_content_item', columnNames: 'id,pci_access_start,pci_access_end,pci_removed_ts')
			}
		}
		createIndex(indexName: "pci_id_dates_idx", tableName: "package_content_item") {
			column(name: "id")
			column(name: "pci_access_start")
			column(name: "pci_access_end")
			column(name: "pci_removed_ts")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-008") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'identifier', columnNames: 'id_value,id_ns_fk')
			}
		}
		createIndex(indexName: "identifier_id_value_id_ns_fk_idx", tableName: "identifier") {
			column(name: "id_value")
			column(name: "id_ns_fk")
		}
	}

	changeSet(author: "efreestone (manual)", id: "2024-02-15-009") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'identifier_occurrence', columnNames: 'io_identifier_fk,io_res_fk')
			}
		}
		createIndex(indexName: "identifier_occurrence_io_identifier_fk_io_res_fk_idx", tableName: "identifier_occurrence") {
			column(name: "io_identifier_fk")
			column(name: "io_res_fk")
		}
	}
}