set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [24.49763217	67.64342349	140.7589897	250.1460469	1410.909602];
y2 = [24.14168962	63.14775818	123.3497535	181.5691915	933.710148];
y3 = [13.51286334	14.59760266	19.53124851	19.41142	21.99730351];

p1= plot(x, y1, '-kv');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('percentage of keywords');
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