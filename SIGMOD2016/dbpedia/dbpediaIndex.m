set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [15252.333333333334	28382.333333333332	54501.666666666664	99245.0	175878.66666666666];
y2 = [7608.333333333333	14215.0	35639.0	76166.33333333333	146000.66666666666];
y3 = [7491.333333333333	19147.0	31649.333333333332	45313.333333333336	66325.66666666667];
y4 = [1685.6666666666667	6839.333333333333	14829.0	20893.666666666668	31797.666666666668];


p1= plot(x, y1, '-k^');
hold on;
p2 = plot(x, y2, '-kv');
hold on;
p3 = plot(x, y3, '-ks');
hold on;
p4 = plot(x, y4, '-kd');

xlabel('percentage of vertices');
ylabel('time (ms)');

axis([0.5 5.5 0.0 190000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
leg=legend('Basic','Basic-','Advanced', 'Advanced-', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);