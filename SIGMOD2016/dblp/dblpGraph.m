set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [14.05637945	51.19207884	789.2536863	1027.539136	1410.909602];
y2 = [9.267643383	28.52915486	599.5229233	700.1131298	933.710148];
y3 = [1.107347977	4.377263643	10.38925361	15.35749973	21.99730351];

p1= plot(x, y1, '-kv');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('percentage of vertices');
ylabel('time (ms)');

axis([0.5 5.5 0.0 1500]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
leg=legend('Inc-S','Inc-T','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);