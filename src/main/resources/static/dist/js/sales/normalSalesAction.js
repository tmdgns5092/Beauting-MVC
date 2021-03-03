/* 일반 결제 (제품 + 서비스) */
function allSaleInsert() {

    // 시술
    var cateIdxArray = new Array();
    var service_resource_list = [];
    var service_use_prepaid_list = [];
    var service_use_ticket_list = [];
    var service_total_exhaust = 0;
    // var service_total_cost = 0;
    var service_resource = '';
    var service_use_prepaid = '';
    var service_use_ticket = '';
    var service_total_miss = parseInt(uk_noshow(uncomma($('#sale-main-total-miss').text())));
    var service_use_total_cost, service_use_total_exhaust;



    if (service_total_miss > 0) {
        service_use_total_cost = parseInt(uk_noshow(uncomma($('#sale-main-total-money').text()))) + parseInt(uk_noshow(uncomma($('#sale-main-total-card').text())));
        service_use_total_exhaust = parseInt(uk_noshow(uncomma($('#sale-main-total-gift').text()))) + parseInt(uk_noshow(uncomma($('#sale-main-total-other').text()))) + parseInt(uk_noshow(uncomma($('#sale-main-total-miss').text())));
    }

    // 프로모션
    var promotion_target_surgery = {};
    var promotion;
    $.ajax({
        type: 'POST',
        url: '/Sales/promotionSelect',
        dataType: 'json',
        data: null,
        async: false,
        success: function (data) {
            if (data.code == 200) {
                promotion = data.promotion;
            } else if (data.code == 902) console.log('---------------이달 프로모션 없음');
            else console.log('--------------------코드에러');
        },
        error: function (error) {
            console.log(error.status);
        }
    });
    // 제품
    var product_resource_list = [];
    var product_use_prepaid_list = [];
    var product_use_ticket_list = [];
    var product_total_exhaust = 0;
    var product_total_cost = 0;
    var product_resource = '';
    var product_use_prepaid = '';
    var product_use_ticket = '';
    var product_total_miss = parseInt(uk_noshow(uncomma($('#product-sale-main-total-miss').text())));
    var product_use_total_cost, product_use_total_exhaust;
    if (product_total_miss > 0) {
        product_use_total_cost = parseInt(uk_noshow(uncomma($('#product-sale-main-total-money').text()))) + parseInt(uk_noshow(uncomma($('#product-sale-main-total-card').text())));
        product_use_total_exhaust = parseInt(uk_noshow(uncomma($('#product-sale-main-total-gift').text()))) + parseInt(uk_noshow(uncomma($('#product-sale-main-total-other').text()))) + parseInt(uk_noshow(uncomma($('#product-sale-main-total-point').text())));
    } else {
        product_use_total_cost = parseInt(uk_noshow(uncomma($('#product-sale-main-total-money').text()))) + parseInt(uk_noshow(uncomma($('#product-sale-main-total-card').text())));
        product_use_total_exhaust = parseInt(uk_noshow(uncomma($('#product-sale-main-total-gift').text()))) + parseInt(uk_noshow(uncomma($('#product-sale-main-total-other').text()))) + parseInt(uk_noshow(uncomma($('#product-sale-main-total-point').text())));
    }

    // 고객
    var prepaid_use_array = new Array();
    var ticket_use_array = new Array();
    // 선불권
    $('div[name=prepaid-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            var tmp = {
                sal_idx: parseInt($(value).find('input[name=sal_idx]').val()) + "",
                use_cost: parseInt($(value).find('input[name=use-cost]').val()) + "",
                prepaid_name: $(value).find('input[name=name]').val() + "",
                total_cost: $(value).find('input[name=cost]').val()
            };
            prepaid_use_array.push(tmp);
        }
    });
    $('div[name=product-prepaid-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            var sal_idx = $(value).find('input[name=sal_idx]').val();
            var use_cost = $(value).find('input[name=use-cost]').val();
            var prepaid_name = $(value).find('input[name=name]').val();
            var total_cost = $(value).find('input[name=cost]').val();
            var push_flag = true;

            $.each(prepaid_use_array, function (k, v) {
                if (v.sal_idx == sal_idx) {
                    prepaid_use_array[k].use_cost = parseInt(Number(prepaid_use_array[k].use_cost) + Number(use_cost)) + "";
                    push_flag = false;
                }
            });
            if (push_flag) {
                var tmp = {
                    sal_idx: parseInt(sal_idx) + "",
                    use_cost: parseInt(use_cost.toString()) + "",
                    prepaid_name: prepaid_name,
                    total_cost: total_cost
                };
                prepaid_use_array.push(tmp);
            }
        }
    });
    // 티켓
    $('div[name=ticket-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            var tmp = {
                sal_idx: $(value).find('input[name=sal_idx]').val() + "",
                use_count: parseInt($(value).find('input[name=use-count]').val()) + "",
                ticket_name: $(value).find('input[name=name]').val() + "",
                total_count: $(value).find('input[name=count]').val()
            };
            ticket_use_array.push(tmp);
        }
    });
    $('div[name=product-ticket-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            var sal_idx = $(value).find('input[name=sal_idx]').val();
            var use_count = $(value).find('input[name=use-count]').val();
            var ticket_namee = $(value).find('input[name=name]').val();
            var total_count = $(value).find('input[name=count]').val();
            var push_flag = true;

            $.each(ticket_use_array, function (k, v) {
                if (v.sal_idx == sal_idx) {
                    ticket_use_array[k].use_count = parseInt(Number(ticket_use_array[k].use_count) + Number(use_count)) + "";
                    push_flag = false;
                }
            });
            if (push_flag) {
                var tmp = {
                    sal_idx: sal_idx + "",
                    use_count: parseInt(use_count.toString()) + "",
                    ticket_namee: ticket_namee + "",
                    total_count: total_count
                };
                ticket_use_array.push(tmp);
            }
        }
    });

    //console.log(JSON.stringify(prepaid_use_array));
    //console.log(JSON.stringify(ticket_use_array));

    /* Service */
    $('table.sale-main-table > tbody > tr').each(function (index, value) {
        // 카테고리 idx 가져오기;
        var cateObject = {name: "idx", cate_idx: $(value).find('input[name=cate_idx]').val()};
        var addFlag = true;
        $.each(cateIdxArray, function (i, v) {
            if (v.cate_idx == $(value).find('input[name=cate_idx]').val()) addFlag = false;
        });
        if (addFlag) cateIdxArray.push(cateObject);


        // var service_exhaust = parseInt(uk_noshow(uncomma($(value).find('td[name=one_cost]').text()))) * parseInt(uk_noshow(uncomma($(value).find('td[name=count]').find('p[name=count]').text())));
        var dc = $(value).find('td[name=dc]').find('input[name=dc]').val();
        var count = $(value).find('td[name=count]').find('p[name=count]').text();
        var services_idx = $(value).attr('data-value');
        var services_cost = uk_noshow(uncomma($(value).find('td[name=one_cost]').text()));
        var sales_fee = (parseInt(services_cost) * parseInt(uk_noshow(count))) - parseInt(dc);
        var services_name = $(value).find('td[name=name]').text();
        var services_category = $(value).find('td[name=name]').attr('data-value');

        var this_cost_v2 = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=cost_text]').val()));
        var exhaust_cost_v2 = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));

        // 임의로 판매 금액을 증가시켰을 경우 sales fee 를 증가 값으로 받는다.
        if (exhaust_cost_v2 + this_cost_v2 > sales_fee) {
            service_total_exhaust += exhaust_cost_v2 + this_cost_v2;        // 전체 소진
            sales_fee = exhaust_cost_v2 + this_cost_v2;                     // 오브젝트 소진
        } else {
            service_total_exhaust += sales_fee;
        }


        // service_total_cost += parseInt($(value).find('td[name=cost]').find('input[name=cost_text]').val());

        var empl1_idx = $(value).find('td[name=empl]').find('input[name=empl_idx1]').val();
        var empl1_name = $(value).find('td[name=empl]').find('input[name=empl_name1]').val();
        var empl2_idx = $(value).find('td[name=empl]').find('input[name=empl_idx2]').val();
        var empl2_name = $(value).find('td[name=empl]').find('input[name=empl_name2]').val();

        var use_accumulate = $(value).find('td[name=cost]').find('input[name=exhaust_cost]').val();
        var use_name = $(value).find('td[name=cost]').find('input[name=exhaust_name]').val();

        var this_cost_text = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=cost_text]').val()));

        // 미수금 없음 (선불권 / 티켓으로 미수금 완전 처리)
        if (this_cost_text == 0) {
            //console.log("미수금 없음 (선불권 / 티켓으로 미수금 완전 처리)");
            var empl1_cost = 0;
            var empl1_exhaust = parseInt(sales_fee / 2);
            var empl2_cost = 0;
            var empl2_exhaust = parseInt(sales_fee / 2);
        }
        // 소진 처리
        else if (service_total_miss > 0 && service_use_total_exhaust > 0 && this_cost_text > 0) {
            //console.log("소진 처리 ... total exhaust : " + service_use_total_exhaust + ", this cost : " + this_cost_text);
            // 소진으로 미수금을 다 깍을 수 있는 경우
            if (sales_fee <= service_use_total_exhaust) {
                //console.log("소진으로 미수금을 다 깍을 수 있는 경우");
                var empl1_cost = 0;
                var empl1_exhaust = parseInt(sales_fee / 2);
                var empl2_cost = 0;
                var empl2_exhaust = parseInt(sales_fee / 2);

                service_use_total_exhaust = service_use_total_exhaust - sales_fee;
            }
            // 상품권이 미수금에 못미치는 경우
            else {
                //console.log("상품권이 미수금에 못미치는 경우");
                this_cost_text = this_cost_text - service_use_total_exhaust;

                // 소진으로 미수금 처리를 다하지 못하고 매출이(현금/카드) 있는 경우
                if (service_use_total_cost > 0 && this_cost_text > 0) {
                    //console.log("소진으로 미수금 처리를 다하지 못하고 매출이(현금/카드) 있는 경우");
                    // 매출로 모든 미수금을 없앨 수 있을 경우
                    if (service_use_total_cost >= this_cost_text) {
                        //console.log("매출로 모든 미수금을 없앨 수 있을 경우" + this_cost_text + ", " + sales_fee);
                        var empl1_cost = parseInt(this_cost_text / 2);
                        var empl1_exhaust = parseInt(sales_fee / 2);
                        var empl2_cost = parseInt(this_cost_text / 2);
                        var empl2_exhaust = parseInt(sales_fee / 2);
                        service_use_total_cost = service_use_total_cost - this_cost_text;
                    }
                    // 매출로 모든 미수금을 없앨 수 없는 경우
                    else {
                        //console.log("매출로 모든 미수금을 없앨 수 없는 경우");
                        var tmp_exhaust = Number(use_accumulate) + Number(service_use_total_exhaust + service_use_total_cost);
                        //console.log("service total exhaust : " + service_use_total_exhaust);
                        //console.log("service total cost : " + service_use_total_cost);
                        //console.log("use_accumulate : " + use_accumulate);
                        //console.log("tmp exhaust : " + tmp_exhaust);
                        var empl1_cost = parseInt(service_use_total_cost / 2);
                        var empl1_exhaust = parseInt(tmp_exhaust / 2);
                        var empl2_cost = parseInt(service_use_total_cost / 2);
                        var empl2_exhaust = parseInt(tmp_exhaust / 2);
                        service_use_total_cost = 0;
                    }
                }
                // 소진으로 미수금 처리를 다 하지 못하고 매출이(현금/카드) 없는 경우
                else {
                    //console.log("소진으로 미수금 처리를 다 하지 못하고 매출이(현금/카드) 없는 경우");
                    var empl1_cost = 0;
                    var empl1_exhaust = parseInt(this_cost_text / 2);
                    var empl2_cost = 0;
                    var empl2_exhaust = parseInt(this_cost_text / 2);
                }
                service_use_total_exhaust = 0;
            }
        }
        // 매출 처리
        else if (service_total_miss > 0 && service_use_total_cost > 0 && this_cost_text > 0) {
            //console.log("매출 처리 ...");
            // 매출으로 미수금을 다 깍을 수 있는 경우
            if (this_cost_text <= service_use_total_cost) {
                //console.log("// 매출으로 미수금을 다 깍을 수 있는 경우");
                var empl1_cost = parseInt(this_cost_text / 2);
                var empl1_exhaust = parseInt(this_cost_text / 2);
                var empl2_cost = parseInt(this_cost_text / 2);
                var empl2_exhaust = parseInt(this_cost_text / 2);

                service_use_total_cost = service_use_total_cost - this_cost_text;
            }
            // 현금/카드가 미수금에 못미치는 경우
            else {
                //console.log("// 현금/카드가 미수금에 못미치는 경우");
                this_cost_text = this_cost_text - service_use_total_cost;
                var empl1_cost = parseInt(this_cost_text / 2);
                var empl1_exhaust = parseInt(this_cost_text / 2);
                var empl2_cost = parseInt(this_cost_text / 2);
                var empl2_exhaust = parseInt(this_cost_text / 2);

                service_use_total_cost = 0;
            }
        }
        // 미수금 처리
        else if (service_total_miss > 0 && service_use_total_exhaust < 1 && service_use_total_cost < 1) {
            //console.log("미수금 처리 ...");
            var empl1_cost = 0;
            var empl1_exhaust = 0;
            var empl2_cost = 0;
            var empl2_exhaust = 0;
        }
        // 미수금 없음 (현/카/기/상 으로 미수금 완전 처리)
        else {
            //console.log("미수금 없음 (현/카/기/상 으로 미수금 완전 처리)");
            var empl1_cost = parseInt(parseInt($(value).find('td[name=cost]').find('input[name=cost_text]').val()) / 2);
            var empl1_exhaust = parseInt(sales_fee / 2);
            var empl2_cost = parseInt(parseInt($(value).find('td[name=cost]').find('input[name=cost_text]').val()) / 2);
            var empl2_exhaust = parseInt(sales_fee / 2);
        }

        if (promotion != null) {
            if (services_idx == promotion.services_idx) {
                promotion_target_surgery["idx"] = promotion.idx;
                promotion_target_surgery["achievement_count"] = parseInt(count);
                promotion_target_surgery["achievement_cost"] = parseInt(sales_fee);
                console.log(promotion_target_surgery);
            }
        } else {
            promotion_target_surgery = null;
        }

        if (isNaN(empl1_idx)) empl1_idx = 0;
        if (isNaN(empl1_cost)) empl1_cost = 0;
        if (isNaN(empl2_idx)) empl2_idx = 0;
        if (isNaN(empl2_cost)) empl2_cost = 0;

        service_resource = {
            dc: dc + "",
            count: count + "",
            sales_fee: sales_fee + "",
            services_idx: services_idx + "",
            services_cost: services_cost + "",
            services_name: services_name + "",
            services_category: services_category + "",
            empl1_idx: empl1_idx + "",
            empl1_cost: empl1_cost + "",
            empl1_name: removeSpaces(empl1_name) + "",
            empl1_exhaust: empl1_exhaust + "",
            empl2_idx: empl2_idx + "",
            empl2_cost: empl2_cost + "",
            empl2_name: removeSpaces(empl2_name) + "",
            empl2_exhaust: empl2_exhaust + "",
            use_accumulate: use_accumulate + "",
            use_name: use_name
        };


        if (uk(empl2_idx) == "" && uk(empl2_name) == "") {
            delete service_resource.empl2_idx;
            delete service_resource.empl2_cost;
            delete service_resource.empl2_name;
            delete service_resource.empl2_exhaust;

            service_resource.empl1_cost = service_resource.empl1_cost * 2 + "";
            service_resource.empl1_exhaust = service_resource.empl1_exhaust * 2 + "";
        }

        //console.log(JSON.stringify(service_resource));

        service_resource_list.push(service_resource);
    });
    /* service prepaid */
    $('div[name=prepaid-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            service_use_prepaid = {
                idx: $(value).find('input[name=idx]').val() + "",
                sale_idx: $(value).find('input[name=sal_idx]').val() + "",
                name: $(value).find('input[name=name]').val() + "",
                cost: $(value).find('input[name=use-cost]').val() + ""
            };
            service_use_prepaid_list.push(service_use_prepaid);
        }
    });
    /* service ticket */
    $('div[name=ticket-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            service_use_ticket = {
                idx: $(value).find('input[name=idx]').val() + "",
                sale_idx: $(value).find('input[name=sal_idx]').val() + "",
                name: $(value).find('input[name=name]').val() + "",
                count: $(value).find('input[name=use-count]').val() + ""
            };
            service_use_ticket_list.push(service_use_ticket);
        }
    });

    /* Product */
    $('table.product-sale-main-table > tbody > tr').each(function (index, value) {
        //console.log(index);
        var dc = parseInt($(value).find('td[name=dc]').find('input[name=dc]').val());
        var count = parseInt($(value).find('td[name=count]').find('p[name=count]').text());
        var one_cost = parseInt($(value).find('td[name=one_cost]').text());
        var services_idx = $(value).attr('data-value');
        var services_cost = uk_noshow(uncomma($(value).find('td[name=one_cost]').text()));
        var sales_fee = (parseInt(services_cost) * parseInt(uk_noshow(count))) - parseInt(dc);
        var services_name = $(value).find('td[name=name]').text();
        var services_category = $(value).find('td[name=name]').attr('data-value');

        var this_cost_v2 = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=cost_text]').val()));
        var exhaust_cost_v2 = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));

        // 임의로 판매 금액을 증가시켰을 경우 sales fee 를 증가 값으로 받는다.
        if (exhaust_cost_v2 + this_cost_v2 > sales_fee) {
            sales_fee = exhaust_cost_v2 + this_cost_v2;                     // 오브젝트 소진
        }

        // 제품 매출, 소진, 소진 종류 정의
        var this_cost, this_exhaust, this_exhaust_type, this_exhaust_name, use_accumulate;
        // 제품 - 소진이 없을 경우
        if ($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == "0") {
            this_cost = parseInt($(value).find('td[name=cost]').find('input[name=cost_text]').val());
            this_exhaust = 0;
            use_accumulate = 0;
            this_exhaust_name = '';
        }
        // 제품 - 소진 발생 (선불권)
        else if ($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == "1") {
            this_cost = $(value).find('td[name=cost]').find('input[name=cost_text]').val();
            this_exhaust = parseInt($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val());
            use_accumulate = this_exhaust;
            this_exhaust_name = $(value).find('td[name=cost]').find('input[name=exhaust_name]').val();
        }
        // 제품 - 소진 발생 (회원권)
        else if ($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == "2") {
            this_cost = 0;
            this_exhaust = one_cost * count - dc;
            use_accumulate = 0;
            this_exhaust_name = $(value).find('td[name=cost]').find('input[name=exhaust_name]').val();
        }


        // product_total_exhaust += sales_fee; // 변경 =>  티켓 & 선불권
        product_total_cost += parseInt($(value).find('td[name=cost]').find('input[name=cost_text]').val());
        product_total_exhaust += this_exhaust; // 변경 =>  티켓 & 선불권
        // product_total_cost += parseInt(this_cost);


        var this_cost_text = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=cost_text]').val()));

        var empl1_idx = $(value).find('td[name=empl]').find('input[name=empl_idx1]').val();
        var empl1_name = $(value).find('td[name=empl]').find('input[name=empl_name1]').val();
        var empl2_idx = $(value).find('td[name=empl]').find('input[name=empl_idx2]').val();
        var empl2_name = $(value).find('td[name=empl]').find('input[name=empl_name2]').val();

        // 미수금 없음 (선불권 / 티켓으로 미수금 완전 처리)
        if (this_cost_text == 0) {
            //console.log('제품-미수금 없음 ...');
            var empl1_cost = 0;
            var empl1_exhaust = parseInt(sales_fee / 2);
            var empl2_cost = 0;
            var empl2_exhaust = parseInt(sales_fee / 2);
        }
        // 소진 처리
        else if (product_total_miss > 0 && product_use_total_exhaust > 0 && this_cost_text > 0) {
            //console.log("소진 처리 ...");
            // 소진으로 미수금을 다 깍을 수 있는 경우
            if (this_cost_text <= product_use_total_exhaust) {
                var empl1_cost = 0;
                var empl1_exhaust = parseInt(this_cost_text / 2);
                var empl2_cost = 0;
                var empl2_exhaust = parseInt(this_cost_text / 2);

                product_use_total_exhaust = product_use_total_exhaust - this_cost_text;
            }
            // 상품권이 미수금에 못미치는 경우
            else {
                this_cost_text = this_cost_text - product_use_total_exhaust;

                // 소진으로 미수금 처리를 다하지 못하고 매출이(현금/카드) 있는 경우
                if (product_use_total_cost > 0 && this_cost_text > 0) {
                    var empl1_exhaust = parseInt(product_use_total_exhaust / 2);
                    var empl2_exhaust = parseInt(product_use_total_exhaust / 2);

                    // 매출로 모든 미수금을 없앨 수 있을 경우
                    if (product_use_total_cost >= this_cost_text) {
                        var empl1_cost = parseInt(this_cost_text / 2);
                        var empl2_cost = parseInt(this_cost_text / 2);
                        product_use_total_cost = product_use_total_cost - this_cost_text;
                    }
                    // 매출로 모든 미수금을 없앨 수 없는 경우
                    else {
                        var empl1_cost = parseInt(product_use_total_cost / 2);
                        var empl2_cost = parseInt(product_use_total_cost / 2);
                        product_use_total_cost = 0;
                    }
                }
                // 소진으로 미수금 처리를 다 하지 못하고 매출이(현금/카드) 없는 경우
                else {
                    var empl1_cost = 0;
                    var empl1_exhaust = parseInt(this_cost_text / 2);
                    var empl2_cost = 0;
                    var empl2_exhaust = parseInt(this_cost_text / 2);
                }
                product_use_total_exhaust = 0;
            }
        }
        // 매출 처리
        else if (product_total_miss > 0 && product_use_total_cost > 0 && this_cost_text > 0) {
            //console.log('제품 매출 ...');
            // 매출으로 미수금을 다 깍을 수 있는 경우
            if (this_cost_text <= product_use_total_cost) {
                var empl1_cost = parseInt(this_cost_text / 2);
                var empl1_exhaust = 0;
                var empl2_cost = parseInt(this_cost_text / 2);
                var empl2_exhaust = 0;

                product_use_total_cost = product_use_total_cost - this_cost_text;
            }
            // 현금/카드가 미수금에 못미치는 경우
            else {
                this_cost_text = this_cost_text - product_use_total_cost;
                var empl1_cost = parseInt(this_cost_text / 2);
                var empl1_exhaust = 0;
                var empl2_cost = parseInt(this_cost_text / 2);
                var empl2_exhaust = 0;

                product_use_total_cost = 0;
            }
        }
        // 미수금 처리
        else if (product_total_miss > 0 && product_use_total_exhaust < 1 && product_use_total_cost < 1) {
            //console.log('제품 미수 ...');
            var empl1_cost = 0;
            var empl1_exhaust = 0;
            var empl2_cost = 0;
            var empl2_exhaust = 0;
        }
        // 미수금 없음 (현/카/기/상 으로 미수금 완전 처리)
        else {
            //console.log("미수금 없음 (현/카/기/상 으로 미수금 처리 완료) ...");
            // var empl1_cost = parseInt(product_use_total_cost / 2);
            // var empl2_cost = parseInt(product_use_total_cost / 2);
            // var empl1_exhaust = parseInt(product_use_total_exhaust / 2);
            // var empl2_exhaust = parseInt(product_use_total_exhaust / 2);

            //console.log("남은 소진 : " + product_use_total_exhaust);
            //console.log("남은 매출 : " + product_use_total_cost);
            //console.log("결제할 금액 : " + sales_fee);
            // 소진
            if (sales_fee <= parseInt(product_use_total_exhaust)) {
                var empl1_cost = 0;
                var empl2_cost = 0;
                var empl1_exhaust = parseInt(sales_fee / 2);
                var empl2_exhaust = parseInt(sales_fee / 2);

                product_use_total_exhaust = product_use_total_exhaust - sales_fee;
            }
            else if (sales_fee <= parseInt(product_use_total_cost)) {
                var empl1_cost = parseInt(sales_fee / 2);
                var empl2_cost = parseInt(sales_fee / 2);
                var empl1_exhaust = 0;
                var empl2_exhaust = 0;

                product_use_total_cost = product_use_total_cost - sales_fee;
            }
        }

        // var use_name = $(value).find('td[name=cost]').find('input[name=exhaust_name]').val();
        var use_name = this_exhaust_name;

        if (isNaN(empl1_idx)) empl1_idx = 0;
        if (isNaN(empl1_cost)) empl1_cost = 0;
        if (isNaN(empl2_idx)) empl2_idx = 0;
        if (isNaN(empl2_cost)) empl2_cost = 0;

        product_resource = {
            dc: dc + "",
            count: count + "",
            sales_fee: sales_fee + "",
            services_idx: services_idx + "",
            services_cost: services_cost + "",
            services_name: services_name + "",
            services_category: services_category + "",
            empl1_idx: uk_noshow(empl1_idx) + "",
            empl1_cost: uk_noshow(empl1_cost) + "",
            empl1_name: removeSpaces(empl1_name) + "",
            empl1_exhaust: uk_noshow(empl1_exhaust) + "",
            empl2_idx: uk_noshow(empl2_idx) + "",
            empl2_cost: uk_noshow(empl2_cost) + "",
            empl2_name: removeSpaces(empl2_name) + "",
            empl2_exhaust: uk_noshow(empl2_exhaust) + "",
            use_accumulate: use_accumulate + "",
            use_name: use_name
        };

        if (uk(empl2_idx) == "" && uk(empl2_name) == "") {
            delete product_resource.empl2_idx;
            delete product_resource.empl2_cost;
            delete product_resource.empl2_name;
            delete product_resource.empl2_exhaust;

            product_resource.empl1_cost = product_resource.empl1_cost * 2 + "";
            product_resource.empl1_exhaust = product_resource.empl1_exhaust * 2 + "";
        }

        //console.log("제품 오브젝트" + JSON.stringify(product_resource));
        product_resource_list.push(product_resource);
    });

    /* product prepaid */
    $('div[name=product-prepaid-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            product_use_prepaid = {
                idx: $(value).find('input[name=idx]').val() + "",
                sale_idx: $(value).find('input[name=sal_idx]').val() + "",
                name: $(value).find('input[name=name]').val() + "",
                cost: $(value).find('input[name=use-cost]').val() + ""
            };
            product_use_prepaid_list.push(product_use_prepaid);
        }
    });
    /* product ticket */
    $('div[name=product-ticket-body] > button').each(function (index, value) {
        if ($(value).hasClass('active')) {
            product_use_ticket = {
                idx: $(value).find('input[name=idx]').val() + "",
                sale_idx: $(value).find('input[name=sal_idx]').val() + "",
                name: $(value).find('input[name=name]').val() + "",
                count: $(value).find('input[name=use-count]').val() + ""
            };
            product_use_ticket_list.push(product_use_ticket);
        }
    });

    /* 시술 json to string */
    var service_json_array = JSON.stringify(service_resource_list);
    var service_json_prepaid = JSON.stringify(service_use_prepaid_list);
    var service_json_ticket = JSON.stringify(service_use_ticket_list);
    // service_total_cost = service_total_cost - parseInt(uncomma($('#sale-main-total-miss').text()));
    // service_total_cost = parseInt(uk_noshow(uncomma($('#sale-main-total-money').text()))) + parseInt(uk_noshow(uncomma($('#sale-main-total-card').text())));
    /* 제품 json to string */
    var product_json_array = JSON.stringify(product_resource_list);
    var product_json_prepaid = JSON.stringify(product_use_prepaid_list);
    var product_json_ticket = JSON.stringify(product_use_ticket_list);
    product_total_cost = product_total_cost - parseInt(uncomma($('#product-sale-main-total-miss').text()));

    /* 고객 소진 티켓/선불권 */
    var client_use_prepaid = JSON.stringify(prepaid_use_array);
    var client_use_ticket = JSON.stringify(ticket_use_array);


    if (service_resource_list.length < 1 && product_resource_list.length < 1) {
        alert("판매할 시술 / 제품을 선택해 주세요.");
        return false;
    }

    //console.log("최종 판매 리소스 : " + product_resource_list);

    if (Number(uncomma($('#sale-main-total-miss').text())) > 0 || Number(uncomma($('#product-sale-main-total-miss').text())) > 0) {
        var con = confirm("미수금이 발생합니다. 결제를 계속 진행 하시겠습니까?");
        if (con != true) return false;
    }

    if (promotion_target_surgery != null) {
        $.post('/Sales/updatePromotionAchievement', promotion_target_surgery, function (data) {
            if (data.code == 200) {
                console.log('성과 처리 완료');
            } else {
                alert('프로모션 성과를 처리하는 도중 오류가 발생했습니다.\r\n잠시후 다시 시도해주십시오.');
                form_submit(document.URL, {
                    payment: $('#reload-resource-payment').val(),
                    forDate: $('#reload-resource-fordate').val(),
                    client_idx: $('#reload-resource-client-idx').val()
                });
            }
        }, 'json').fail(function (error) {
            // alert('프로모션 성과를 처리하는 도중 오류가 발생했습니다.\r\n잠시후 다시 시도해주십시오.');
            // form_submit(document.URL, {payment : $('#reload-resource-payment').val(), forDate : $('#reload-resource-fordate').val(), client_idx : $('#reload-resource-client-idx').val()});
            console.log(error.status);
        });
    }

    $.ajax({
        url: "/Sales/normalSalesInsert",
        type: "post",
        dataType: "json",
        data: {
            "res_idx": res_idx,
            "memo": $('#fn-total-sales-memo').val(),
            "client_idx": uk_noshow(client_idx),
            "store_point": uncomma($('#store-point-view').text()),
            "now": dateNOW(),
            "client_use_prepaid": client_use_prepaid,
            "client_use_ticket": client_use_ticket,
            "client_use_point": Number(uncomma($('#sale-main-total-point').text())) + Number(uncomma($('#product-sale-main-total-point').text())),
            "total_prepaid_cost": uncomma($('#final-modal-ex-cost').text()),
            "serviceCateIdxArray": JSON.stringify(cateIdxArray),

            // 서비스
            "service_sale_resource": service_json_array + "",
            "service_use_prepaid": service_json_prepaid + "",
            "service_use_ticket": service_json_ticket + "",
            // "service_total_exhaust" : parseInt(Number(service_total_exhaust) - Number(parseInt(uk_noshow(uncomma($('#sale-main-total-miss').text()))))),
            "service_total_exhaust": parseInt(service_total_exhaust),
            "service_total_miss_cost": uk_noshow(uncomma($('#sale-main-total-miss').text())),
            "service_total_cost": parseInt(uk_noshow(uncomma($('#sale-main-total-money').text()))) + parseInt(uk_noshow(uncomma($('#sale-main-total-card').text()))),
            "service_total_money": uk_noshow(uncomma($('#sale-main-total-money').text())),
            "service_total_card": uncomma($('#sale-main-total-card').text()),
            "service_total_gift": uncomma($('#sale-main-total-gift').text()),
            "service_total_other": uncomma($('#sale-main-total-other').text()),
            "service_total_point": uncomma($('#sale-main-total-point').text()),
            "service_size": service_resource_list.length,

            // 제품
            "product_sale_resource": product_json_array + "",
            "product_use_prepaid": product_json_prepaid + "",
            "product_use_ticket": product_json_ticket + "",
            "product_total_exhaust": product_total_exhaust,
            "product_total_miss_cost": uk_noshow(uncomma($('#product-sale-main-total-miss').text())),
            "product_total_cost": product_total_cost,
            "product_total_money": uk_noshow(uncomma($('#product-sale-main-total-money').text())),
            "product_total_card": uncomma($('#product-sale-main-total-card').text()),
            "product_total_gift": uncomma($('#product-sale-main-total-gift').text()),
            "product_total_other": uncomma($('#product-sale-main-total-other').text()),
            "product_total_point": uncomma($('#product-sale-main-total-point').text()),
            "product_size": product_resource_list.length


            // 고객
        },
        success: function (data) {
            //console.log("data code : " + data.code);
            if (data.code == 200) {
                /*중복 클리 방지 풀어줌*/
                insert_bool = true;
                alert("결제가 완료되었습니다.");
                var url = ctx + '/Reservation/calendar';
                var form = document.createElement("form");
                $(form).attr("action", url).attr("method", "post");
                $(form).html('<input type="hidden" name="forDate" value="' + forDate + '" />');
                document.body.appendChild(form);
                $(form).submit();
                document.body.removeChild(form);

            } else if (data.code == 201) {
                /*중복 클리 방지 풀어줌*/
                insert_bool = true;
                alert("결제가 완료되었습니다.");
                location.href = ctx + '/Sales/searchClient';
            } else {
                /*중복 클리 방지 풀어줌*/
                insert_bool = true;
                alert("잠시 후 다시 시도해 주세요");
                return false;
            }
        },
        error: function () {
            /*중복 클리 방지 풀어줌*/
            insert_bool = true;
            alert("에러가 발생했습니다.");
            location.href = document.URL;
        }
    });


}